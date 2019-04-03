import 'package:flutter/material.dart';
import 'package:flutter/services.dart';


/// Created by wangzs on 2019-02-15 17:56
/// E-Mail Address：kingkadienm@163.com

class plugin_test {
  //获取到插件与原生的交互通道
  static const jumpPlugin = const MethodChannel('com.jzhu.jump/plugin');

  Future<Null> _jumpToNative() async {
    String result = await jumpPlugin.invokeMethod('oneAct');

    print(result);
    return result;
  }

  Future<Null> _jumpToNativeWithValue() async {

    Map<String, String> map = { "flutter": "这是一条来自flutter的参数" };

    String result = await jumpPlugin.invokeMethod('twoAct', map);

    print(result);
    return result;
  }

}


