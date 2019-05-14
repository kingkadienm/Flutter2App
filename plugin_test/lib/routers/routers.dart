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
