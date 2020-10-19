import 'package:flutter/services.dart';
typedef void PullEventListener(Map args);
class HwPullViewController {
  final PullEventListener onPullEventListener;

  HwPullViewController(int id, {this.onPullEventListener})
      : _channel = MethodChannel('com.slh.hw_live/HwPullView_$id') {
    _channel.setMethodCallHandler(_onMethodCall);
  }

  final MethodChannel _channel;

  Future<bool> _onMethodCall(MethodCall call) async {
    switch (call.method) {
      case "onPullEvent":
        if (onPullEventListener != null) {
          onPullEventListener(call.arguments as Map);
        }
        break;
    }
    throw MissingPluginException(
        '${call.method} was invoked but has no handler');
  }

  /// 开始播放
  Future start(String url, bool isLive) async {
    assert(url != null);
    return await _channel.invokeMethod('start', <String, dynamic>{
      'url': url,
      "isLive": isLive,
    });
  }


}
