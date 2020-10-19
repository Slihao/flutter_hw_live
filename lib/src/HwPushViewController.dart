import 'package:flutter/services.dart';
typedef void PushEventListener(Map args);

class HwPushViewController {
  final PushEventListener onPushEventListener;

  HwPushViewController(int id, {this.onPushEventListener})
      : _channel = MethodChannel('com.slh.hw_live/HwPushView_$id') {
    _channel.setMethodCallHandler(_onMethodCall);
  }

  final MethodChannel _channel;

  Future<bool> _onMethodCall(MethodCall call) async {
    switch (call.method) {
      case "onPushEvent":
        if (onPushEventListener != null) {
          onPushEventListener(call.arguments as Map);
        }
        break;
    }
    throw MissingPluginException(
        '${call.method} was invoked but has no handler');
  }

  /// 开始推流
  Future startPublish(String url) async {
    assert(url != null);
    return await _channel.invokeMethod('startPublish', <String, dynamic>{
      'url': url,
    });
  }

  /// 结束推流
  Future stopPublish() async {
    return await _channel.invokeMethod('stopPublish', {});
  }

  /// 开启摄像头预览
  Future startCameraPreview() async {
    return await _channel.invokeMethod('startCameraPreview', {});
  }

  /// 停止摄像头预览。
  Future stopCameraPreview() async {
    return await _channel.invokeMethod('stopCameraPreview', {});
  }

  /// 暂停摄像头采集并进入垫片推流状态。
  Future pausePusher() async {
    return await _channel.invokeMethod('pausePusher', {});
  }

  /// 恢复摄像头采集并结束垫片推流状态。
  Future resumePusher() async {
    return await _channel.invokeMethod('resumePusher', {});
  }

  /// 查询是否正在推流。 pausePusher 后仍然返回的是true, stopPusher 后才会返回false
  Future<bool> isPushing() async {
    return await _channel.invokeMethod<bool>('isPushing', {});
  }

  /// 设置视频编码质量
  /// 推荐设置：秀场直播 quality：[TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION]；adjustBitrate：false；adjustResolution：false
  Future setVideoQuality(
      int quality, bool adjustBitrate, bool adjustResolution) async {
    return await _channel.invokeMethod('setVideoQuality', {
      "quality": quality,
      "adjustBitrate": adjustBitrate,
      "adjustResolution": adjustResolution
    });
  }

  /// 切换前后摄像头
  Future<bool> cameraSwitch() async {
    return await _channel.invokeMethod<bool>('cameraSwitch', {});
  }

  /// 打开后置摄像头旁边的闪光灯
  Future<bool> flashLightSwitch() async {
    return await _channel.invokeMethod<bool>('flashLightSwitch', {});
  }

  /// 美颜
  Future<bool> beautySwitch() async {
    return await _channel.invokeMethod<bool>('beautySwitch', {});
  }

  Future<bool> close() async {
    return await _channel.invokeMethod<bool>('close', {});
  }
  /// 调整摄像头的焦距
  /// 返回 true：成功； false：失败。
  Future setZoom(int value) async {
    return await _channel.invokeMethod('setZoom', {"value": value});
  }

  ///////////////////////////音频相关接口 /////////////////////////////////
  /// 开启静音。true：静音；false：不静音。
  Future setMute(bool mute) async {
    return await _channel.invokeMethod('setMute', {"mute": mute});
  }
}
