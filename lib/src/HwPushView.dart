import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'HwPushViewController.dart';

typedef void HwPushViewCreatedCallback(HwPushViewController controller);

class HwPushView extends StatefulWidget {
  /// Which gestures should be consumed by the map.
  ///
  /// It is possible for other gesture recognizers to be competing with the map on pointer
  /// events, e.g if the map is inside a [ListView] the [ListView] will want to handle
  /// vertical drags. The map will claim gestures that are recognized by any of the
  /// recognizers on this list.
  ///
  /// When this set is empty or null, the map will only handle pointer events for gestures that
  /// were not claimed by any other gesture recognizer.
  final Set<Factory<OneSequenceGestureRecognizer>> gestureRecognizers;

  final HwPushViewCreatedCallback onHwPushViewCreatedCallback;

  final PushEventListener onPushEventListener;

  const HwPushView(
      {Key key,
      this.gestureRecognizers,
      this.onHwPushViewCreatedCallback,
      this.onPushEventListener})
      : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return _HwPushViewState();
  }
}

class _HwPushViewState extends State<HwPushView> {
  @override
  Widget build(BuildContext context) {
    final Map<String, dynamic> creationParams = <String, dynamic>{};
    if (defaultTargetPlatform == TargetPlatform.android) {
      return AndroidView(
        viewType: 'com.slh.hw_live/HwPushView',
        onPlatformViewCreated: onPlatformViewCreated,
        gestureRecognizers: widget.gestureRecognizers,
        creationParams: creationParams,
        creationParamsCodec: const StandardMessageCodec(),
      );
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      return UiKitView(
        viewType: 'com.slh.hw_live/HwPushView',
        onPlatformViewCreated: onPlatformViewCreated,
        gestureRecognizers: widget.gestureRecognizers,
        creationParams: creationParams,
        creationParamsCodec: const StandardMessageCodec(),
      );
    }

    return Text(
        '$defaultTargetPlatform is not yet supported by the live plugin');
  }

  Future<void> onPlatformViewCreated(int id) async {
    final HwPushViewController controller = HwPushViewController(
      id,
      onPushEventListener: widget.onPushEventListener,
      // onBGMNotify: widget.onBGMNotify,
      // snapshotResult: widget.snapshotResult
    );
    if (widget.onHwPushViewCreatedCallback != null) {
      widget.onHwPushViewCreatedCallback(controller);
    }
  }
}
