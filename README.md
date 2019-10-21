# Android嵌套Flutter(上).md

面一篇简单的说了一下Android原生怎么嵌套Flutter，并且怎么传值。当然那只是最简单的Demo，放到我们的项目里肯定是不行的，所以我就又总结一些，配合fluro(flutter路由管理)一起使用。

1.创建Android项目(这就不多说了)

2.创建flutter项目，在刚才创建的Android项目里和app同级
```text
flutter create -t module plugin_test
```
3.Android依赖flutter

1.进入到刚才创建的flutter项目里,然后进入到.android执行
```text
$cd .android/
$ ./gradlew flutter:assembleDebug
    2.进入到Android项目里的settings.gradle里

include ':app'
setBinding(new Binding([gradle: this]))
evaluate(new File(
        settingsDir.parentFile,
        'Flutter2App/plugin_test/.android/include_flutter.groovy'
))
```
3.Android依赖flutter，进入app里的build.gradle
```
dependencies {
    implementation project(':flutter')
}
```
此外，Android就已经完全依赖与flutter项目了，到现在为止准备工作已经做完

4.创建FlutterMethodHandler

这是一个Android管理类，主要用于Native与Flutter之间信息传递
```java

public class FlutterMethodHandler implements MethodChannel.MethodCallHandler {


    /**
     * 名称可自定义，保持唯一即可
     */
    public static final String CHANNEL_NAME = "com.wangzs.flutter2app";
    public static final String TAG = "FlutterMethodHandler";
    private MethodChannel methodChannel;

    private FlutterView flutterView;

    private Activity mActivity;

    private FlutterMethodHandler() {

    }

    private static FlutterMethodHandler instance;

    public static FlutterMethodHandler getInstance() {
        if (instance == null) {
            synchronized (FlutterMethodHandler.class) {
                if (instance == null) {
                    instance = new FlutterMethodHandler();
                }
            }
        }
        return instance;
    }

    private FlutterMethodHandler(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 接收Flutter传来的指令，进一步处理
     *
     * @param methodCall
     * @param result
     */
    @Override
    public void onMethodCall(final MethodCall methodCall, final MethodChannel.Result result) {
        if (methodCall == null) {
            return;
        }
        Log.e(TAG, "method is " + methodCall.method + " arguments is " + (methodCall.arguments == null ? "" : methodCall.arguments.toString()));
        Intent intent = new Intent(mActivity, Main2Activity.class);
        switch (methodCall.method) {
            case "withoutParams":
                mActivity.startActivity(intent);
                result.success("success");
                break;
            case "withParams":
                if (methodCall.arguments == null) {
                    return;
                }
                String text = methodCall.arguments.toString();
                intent.putExtra("test", text);
                mActivity.startActivity(intent);
                result.success("success");
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    public MethodChannel getMethodChannel() {
        if (methodChannel == null) {
            methodChannel = new MethodChannel(flutterView, CHANNEL_NAME);
        }
        return methodChannel;
    }

    /**
     * 注册处理回调
     */
    public void registerWith(FlutterView flutterView) {
        this.flutterView = flutterView;
        methodChannel = new MethodChannel(flutterView, CHANNEL_NAME);
        FlutterMethodHandler instance = new FlutterMethodHandler((Activity) flutterView.getContext());
        methodChannel.setMethodCallHandler(instance);
    }
}
```
* onMethodCall：接受Flutter传递到native的信息及其传值，并进行相应逻辑处理
* registerWith：在FlutterActivity里注册，处回调
* getMethodChannel：通过FlutterView，和Channel获取到MethodChannel对象，

向Flutter里发送信号，可以是传递参数也可以是跳转到指定的FlutterView

5.创建FlutterActivity

Native创建FlutterView,并通过Flutter View进行其内部的传值及其调用
```java
public class FlutterActivity extends AppCompatActivity {
    private FlutterView flutterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_flutter_acticity);
        String params = getIntent().getStringExtra("params");
        String route = getIntent().getStringExtra("route");
        FrameLayout rootView = findViewById(R.id.rl_flutter);
        flutterView = Flutter.createView(this, getLifecycle(),route);
        FlutterMethodHandler.getInstance().registerWith(flutterView);
        rootView.addView(flutterView);

        flutterView.post(new Runnable() {
            @Override
            public void run() {
                FlutterMethodHandler.getInstance().getMethodChannel().invokeMethod(route, params);
            }
        });

    }

    /**
     * 屏蔽跳转flutter页面 按下返回键直接推出flutter页面，而不是返回上一级
     */
    @Override
    public void onBackPressed() {
        if (this.flutterView != null) {
            this.flutterView.popRoute();
        } else {
            super.onBackPressed();
        }
    }
}
```
* invokeMethod：Native通过FlutterView和Channel获取到的MethodChannel对象
* 通过invokeMethod向Flutter发送信息

到此Native层的数据已经处理完，接下来开始处理Flutter数据

6.Flutter页面的创建

1.引入依赖，打开pubspec.yaml文件在dependencies处添加依赖，注意一定要与flutter：对齐，不然dart会编译不通过,通过packages get 获取依赖

```
dependencies:
  flutter:
    sdk: flutter
  fluro: ^1.4.0
```
2.Router管理的创建

       routers
          ├──  application.dart           -----定义全局的路由变量
          ├──  router_handler.dart     ----- 创建Router对象，方便全局的引用
          ├──  routers.dart                  -----Flutter页面的定义，以key-value的形式每个key对应唯一的View
          ├──  application.dart  定义全局的路由变量
```dart

import 'package:fluro/fluro.dart';
import 'package:flutter/services.dart';

class Application {
  static Router router;

  static MethodChannel platformChannel;
}
```

    router_handler.dart 创建Router对象，方便全局的引用
```dart
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:fluro/fluro.dart';
import 'package:plugin_test/bean/user.dart';
import 'package:plugin_test/view/one_page.dart';
import 'package:plugin_test/view/two_page.dart';

// app的首页
var onePageHandler = new Handler(
  handlerFunc: (BuildContext context, Map<String, List<String>> params) {
    User bean;
    String param = params['params']?.first;
    if (param != null && param.isNotEmpty) {
      bean = User.fromJson(json.decode(param));
    }
    return OnePage(
      user: bean,
    );
  },
);
var twoPageHandler = new Handler(
  handlerFunc: (BuildContext context, Map<String, List<String>> params) {
    User bean;
    String param = params['params']?.first;
    if (param != null && param.isNotEmpty) {
      bean = User.fromJson(json.decode(param));
    }
    return TwoPage(
      user: bean,
    );
  },
);
```
routers.dart Flutter页面的定义，以key-value的形式每个key对应唯一的View
```dart
import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import './router_handler.dart';

class Routes {
  static String root = "/";
  static String onePage = "/onePage";
  static String twoPage = "/twoPage";

  static void configureRoutes(Router router) {
    router.notFoundHandler = new Handler(
        handlerFunc:
            (BuildContext context, Map<String, List<String>> params) {});
    router.define(onePage, handler: onePageHandler);

    router.define(twoPage, handler: twoPageHandler);
  }
}
```

3.初始化Router

在程序的main.dart里初始化
```dart
void main() {
  SystemChrome.setPreferredOrientations(
      [DeviceOrientation.portraitUp, DeviceOrientation.portraitDown]);
  runApp(new MyApp());
}
class MyApp extends StatelessWidget {
  MyApp() {
    final router = Router();
    Routes.configureRoutes(router);
    Application.router = router;
    Application.platformChannel =
        MethodChannel('com.wangzs.flutter2app');
  }
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: OnePage(),
      onGenerateRoute: Application.router.generator,
    );
  }
}
```

4.页面的创建OnePage
```dart

import 'dart:convert';

import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:plugin_test/bean/user.dart';
import 'package:plugin_test/routers/application.dart';
import 'package:plugin_test/routers/routers.dart';

/// Created by wangzs on 2019-05-12 18:09

class OnePage extends StatefulWidget {
  User user;

  OnePage({this.user});

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return _OnePage();
  }
}

class _OnePage extends State<OnePage> {
  User _user;
  var title = "没有参数";

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    Application.platformChannel.setMethodCallHandler((handler) {
      String _method = handler.method;
      switch (_method) {
        case "onePage":
          if (handler.arguments != null) {
            _user = User.fromJson(json.decode(handler.arguments));
            handlerStateVar();
          }
          break;
      }
    });
  }

  void handlerStateVar() {
    if (_user == null) {
      if (widget.user == null) {
        return;
      }
      _user = widget.user;
    }
    setState(() {
      title = _user.toString();
    });
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      body: ListView(
        children: <Widget>[buildTextView(), buildButtonView()],
      ),
    );
  }

  Widget buildTextView() {
    return Text(title);
  }

  Widget buildButtonView() {
    return RaisedButton(
      onPressed: _intentNextPage,
      child: Text("下一页"),
    );
  }

  _intentNextPage() async {
    if (_user == null) {
      Application.router.navigateTo(context, Routes.twoPage,
          transition: TransitionType.inFromRight /*, clearStack: true*/);
    } else {
      await Application.router.navigateTo(context,
          Routes.twoPage + '?params=${Uri.encodeComponent(json.encode(_user))}',
          transition: TransitionType.inFromRight /*, clearStack: true*/);
    }
  }
}
```


主要方法:_intentNextPage:由刚才定义的Router控制页面的跳转
```
 Application.router.navigateTo(context, Routes.twoPage,
          transition: TransitionType.inFromRight /*, clearStack: true*/);
```
* context：页面的content对象
* Routes.twoPage：具体往那个页面的跳转，由前面Routes定义的

通常我们跳转页面需要进行数据传递，此时就需要通过拼接参数来进行

* Routes.twoPage + '?params=${Uri.encodeComponent(json.encode(_user))}' 

前面在router_handler.dart里我们定义了页面的方法
```dart
var onePageHandler = new Handler(
  handlerFunc: (BuildContext context, Map<String, List<String>> params) {
    User bean;
    String param = params['params']?.first;
    if (param != null && param.isNotEmpty) {
      bean = User.fromJson(json.decode(param));
    }
    return OnePage(
      user: bean,
    );
  },
);
```
* params：就是刚才传递的值
* transition：页面跳转时的动画，从那边进
* clearStack：是否把view从所在的栈里清除

TwoPage
```dart
import 'dart:convert';

import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:plugin_test/bean/user.dart';
import 'package:plugin_test/routers/application.dart';
import 'package:plugin_test/routers/routers.dart';

/// Created by wangzs on 2019-05-12 18:09

class TwoPage extends StatefulWidget {
  User user;

  TwoPage({this.user});

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return _TwoPage();
  }
}

class _TwoPage extends State<TwoPage> {
  User _user;
  var title = "没有参数";

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    Application.platformChannel.setMethodCallHandler((handler) {
      String _method = handler.method;
      switch (_method) {
        case "twoPage":
          if (handler.arguments != null) {
            Map jsonMap = json.decode(handler.arguments);
            _user = User.fromJson(jsonMap);
            handlerStateVar();
          }
          break;
      }
    });
  }

  void handlerStateVar() {
    if (_user == null) {
      if (widget.user == null) {
        return;
      }
      _user = widget.user;
    }
    setState(() {
      title = _user.toString();
    });
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      body: ListView(
        children: <Widget>[buildTextView(), buildButtonView()],
      ),
    );
  }

  Widget buildTextView() {
    return Text(title);
  }

  Widget buildButtonView() {
    return RaisedButton(
      onPressed: _intentNextPage,
      child: Text("跳转到原生"),
    );
  }

  _intentNextPage() async {
    if (_user == null) {
      await Application.platformChannel.invokeMethod("withParams", title);
    } else {
      await Application.platformChannel
          .invokeMethod("withParams", _user.toString());
    }
  }
}
```

前面在Native里创建Flutter的时候说到通过invokeMethod向Flutter控制页面的跳转和传递参数，同理，在Flutter里向Native控制页面跳转和传递参数也是用invokeMethod 
看一下Flutter系统的方法：
```dart
 Future<T> invokeMethod<T>(String method, [ dynamic arguments ]) async {
    assert(method != null);
    final ByteData result = await BinaryMessages.send(
      name,
      codec.encodeMethodCall(MethodCall(method, arguments)),
    );
    if (result == null) {
      throw MissingPluginException('No implementation found for method $method on channel $name');
    }
    final T typedResult = codec.decodeEnvelope(result);
    return typedResult;
  }
```
* method：传递到Native的方法，Native接受的时候需要定义一样的参数
* arguments: 参数的传递


到此Flutter关于参数的传递已经讲解完。

最后附上[Demo](https://github.com/kingkadienm/Flutter2App)地址


---

PS：在导入本项目时，需先进入到plugin_test目录下，执行
flutter packages get  自动生成.androiod  .ios 目录
然后再导入工程

