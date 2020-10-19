import 'package:flutter/services.dart';

class FlutterPluginHwLive {
  static FlutterPluginHwLive instance = new FlutterPluginHwLive();
  final MethodChannel _channel = const MethodChannel('flutter_hw_live');
  init(String licensePath, int mode) {

    _channel.invokeMethod("init",{
      "licensePath":"",
      "mode":1,
    });
  }
}
