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
