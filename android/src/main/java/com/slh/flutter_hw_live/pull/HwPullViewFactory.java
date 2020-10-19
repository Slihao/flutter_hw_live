package com.slh.flutter_hw_live.pull;

import android.app.Activity;
import android.content.Context;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class HwPullViewFactory extends PlatformViewFactory {
    private BinaryMessenger messenger;
    private Activity activity;

    public HwPullViewFactory(BinaryMessenger messenger, Activity activity) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
        this.activity = activity;
    }

    @Override
    public PlatformView create(Context context, int id, Object o) {
        return new HwPullView(messenger, activity, id);
    }
}
