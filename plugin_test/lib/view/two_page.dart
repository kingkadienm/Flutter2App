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
