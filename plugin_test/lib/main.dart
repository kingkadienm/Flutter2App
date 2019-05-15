import 'dart:ui';
import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:plugin_test/view/one_page.dart';
import 'dart:async';

import 'package:plugin_test/routers/application.dart';
import 'package:plugin_test/routers/routers.dart';

/// Created by wangzs on 2019-02-15 17:30
/// E-Mail Address：kingkadienm@163.com

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
//      theme: ThemeData(
//        platform: TargetPlatform.iOS
//      ),
      home: OnePage(),
      onGenerateRoute: Application.router.generator,
    );
  }
}

