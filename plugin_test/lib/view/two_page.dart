import 'package:flutter/material.dart';
import 'package:plugin_test/bean/user.dart';

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
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Center(
      child: ListView(),
    );
  }

  Widget buildTextView(String text) {
    return Text(text);
  }

  Widget buildButtonView() {
    return RaisedButton(
      onPressed: null,
      child: Text("下一页"),
    );
  }
}
