package com.wangzs.flutter2app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import io.flutter.facade.Flutter;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.FlutterView;

/**
 * @description:
 * @autour: wangzs
 * @date:  2019-02-15 17:43
 * @version:
 *
 */
public class FlutterActivity extends AppCompatActivity {
    public static final String FlutterToAndroidCHANNEL = "com.wangzs.native2flutter";
    public static final String AndroidToFlutterCHANNEL = "com.wangzs.flutter2native";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_flutter_acticity);
        FrameLayout frameLayout = findViewById(R.id.rl_flutter);
        FlutterView flutterView = Flutter.createView(this, getLifecycle(), "route");
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        flutterView.setLayoutParams(layoutParams);
        frameLayout.addView(flutterView, layoutParams);
        new EventChannel(flutterView, AndroidToFlutterCHANNEL)
                .setStreamHandler(new EventChannel.StreamHandler() {
                    @Override
                    public void onListen(Object o, EventChannel.EventSink eventSink) {
                        String androidParmas = "来自android原生的参数";
                        eventSink.success(androidParmas);
                    }

                    @Override
                    public void onCancel(Object o) {

                    }
                });


        new MethodChannel(flutterView, FlutterToAndroidCHANNEL).setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {

                //接收来自flutter的指令oneAct
                if (methodCall.method.equals("withoutParams")) {

                    //跳转到指定Activity
                    Intent intent = new Intent(FlutterActivity.this, Main2Activity.class);
                    startActivity(intent);

                    //返回给flutter的参数
                    result.success("success");
                }
                //接收来自flutter的指令twoAct
                else if (methodCall.method.equals("withParams")) {

                    //解析参数
                    String text = methodCall.argument("flutter");

                    //带参数跳转到指定Activity
                    Intent intent = new Intent(FlutterActivity.this, Main2Activity.class);
                    intent.putExtra("test", text);
                    startActivity(intent);

                    //返回给flutter的参数
                    result.success("success");
                } else {
                    result.notImplemented();
                }
            }
        });
    }
}
