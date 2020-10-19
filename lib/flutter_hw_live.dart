// import 'dart:async';
//
// import 'package:flutter/services.dart';
//
// class FlutterHwLive {
//   static const MethodChannel _channel =
//       const MethodChannel('flutter_hw_live');
//
//   static Future<String> get platformVersion async {
//     final String version = await _channel.invokeMethod('getPlatformVersion');
//     return version;
//   }
// }
library flutter_hw_live;
export 'package:flutter_hw_live/src/FlutterPluginHwLive.dart';
export 'package:flutter_hw_live/src/HwPushView.dart';
export 'package:flutter_hw_live/src/HwPushViewController.dart';
export 'package:flutter_hw_live/src/HwPullView.dart';
export 'package:flutter_hw_live/src/HwPullViewController.dart';