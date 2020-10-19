package com.slh.flutter_hw_live;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.meicam.sdk.NvsStreamingContext;
import com.slh.flutter_hw_live.NetworkUtils.NetworkBaseUtils;
import com.slh.flutter_hw_live.pull.HwPullViewFactory;
import com.slh.flutter_hw_live.push.HwPushViewFactory;

import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.platform.PlatformViewRegistry;

/**
 * FlutterHwLivePlugin
 */
public class FlutterHwLivePlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private BinaryMessenger messenger;
    private PlatformViewRegistry platformViewRegistry;
    private Activity activity;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        this.messenger = flutterPluginBinding.getBinaryMessenger();
        this.platformViewRegistry = flutterPluginBinding.getPlatformViewRegistry();
        channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_hw_live");
        channel.setMethodCallHandler(this);
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
//    public static void registerWith(Registrar registrar) {
//        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_hw_live");
//        channel.setMethodCallHandler(new FlutterHwLivePlugin());
//    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        Map<String, Object> request = call.arguments();
        if (call.method.equals("init")) {
            init(activity, "", 1);
            result.success(true);
        } else {
            result.notImplemented();
        }
    }

    public void init(Activity context, String licensePath, int mode) {
        NetworkBaseUtils.init(context);
        NvsStreamingContext.init(context, "");
        if (NvsStreamingContext.getInstance() == null) {
            Log.e("Hw_Live_Plugin", "init失败");
        } else {
            Log.e("Hw_Live_Plugin", "init成功");
        }
    }


    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
        platformViewRegistry.registerViewFactory("com.slh.hw_live/HwPullView",
                new HwPullViewFactory(messenger, binding.getActivity()));
        platformViewRegistry.registerViewFactory("com.slh.hw_live/HwPushView",
                new HwPushViewFactory(messenger, binding.getActivity()));

    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

    }

    @Override
    public void onDetachedFromActivity() {

    }
}
