package com.slh.flutter_hw_live.push;

import android.app.Activity;
import android.content.Context;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class HwPushViewFactory extends PlatformViewFactory {
    private BinaryMessenger messenger;
    private Activity activity;

    public HwPushViewFactory(BinaryMessenger messenger, Activity activity) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
        this.activity = activity;
    }

    @Override
    public PlatformView create(Context context, int id, Object o) {
        return new HwPushView(messenger, activity, id);
    }
}
