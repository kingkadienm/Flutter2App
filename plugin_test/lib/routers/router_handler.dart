import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:fluro/fluro.dart';
import 'package:plugin_test/bean/user.dart';
import 'package:plugin_test/main.dart';
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
