import 'dart:convert';

import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:plugin_test/bean/user.dart';
import 'package:plugin_test/routers/application.dart';
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

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    Application.platformChannel.setMethodCallHandler((handler) {
      String _method = handler.method;
      switch (_method) {
        case "onePage":
          if (handler.arguments != null) {
            _user =  User.fromJson(json.decode(handler.arguments));
          }
          break;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Center(
      child: ListView(
        children: <Widget>[
          buildTextView(_user.toString()),
          buildButtonView()
        ],
      ),
    );
  }

  Widget buildTextView(String text) {
    return Text(text);
  }

  Widget buildButtonView() {
    return RaisedButton(
      onPressed: _intentNextPage,
      child: Text("下一页"),
    );
  }
  _intentNextPage() async{
    await Application.router.navigateTo(
        context,
            '/twoPage?params=${Uri.encodeComponent(json.encode(_user))}',
        transition: TransitionType.inFromRight,
        clearStack: true);
  }
}
