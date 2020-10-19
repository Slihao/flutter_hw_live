import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'HwPullViewController.dart';
typedef void HwPullViewCreatedCallback(HwPullViewController controller);

class HwPullView extends StatefulWidget {
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

  final HwPullViewCreatedCallback onHwPullViewCreatedCallback;

  final PullEventListener onPullEventListener;

  const HwPullView(
      {Key key,
      this.gestureRecognizers,
      this.onHwPullViewCreatedCallback,
      this.onPullEventListener})
      : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return _HwPullViewState();
  }
}

class _HwPullViewState extends State<HwPullView> {
  @override
  Widget build(BuildContext context) {
    final Map<String, dynamic> creationParams = <String, dynamic>{};
    if (defaultTargetPlatform == TargetPlatform.android) {
      return AndroidView(
        viewType: 'com.slh.hw_live/HwPullView',
        onPlatformViewCreated: onPlatformViewCreated,
        gestureRecognizers: widget.gestureRecognizers,
        creationParams: creationParams,
        creationParamsCodec: const StandardMessageCodec(),
      );
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      return UiKitView(
        viewType: 'com.slh.hw_live/HwPullView',
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
    final HwPullViewController controller = HwPullViewController(
      id,
      onPullEventListener: widget.onPullEventListener,
      // onBGMNotify: widget.onBGMNotify,
      // snapshotResult: widget.snapshotResult
    );
    if (widget.onHwPullViewCreatedCallback != null) {
      widget.onHwPullViewCreatedCallback(controller);
    }
  }
  @override
  void dispose() {

    super.dispose();
  }
}
