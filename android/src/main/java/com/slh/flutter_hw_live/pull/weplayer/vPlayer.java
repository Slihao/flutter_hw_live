package com.slh.flutter_hw_live.pull.weplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;


import com.slh.flutter_hw_live.pull.util.LogUtils;

import java.util.Locale;
import java.util.Map;

import tv.danmaku.ijk.media.player.DataReporting.PeriodicReportDate;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class vPlayer extends AbstractPlayer {

    protected IjkMediaPlayer mMediaPlayer;
    private boolean mIsLooping;
    private boolean mIsEnableMediaCodec;
    protected Context mAppContext;
    protected Context mContext;
    private int mBufferedPercent;
    private final String TAG = "vPlayer";


    public vPlayer(Context context) {
        mAppContext = context.getApplicationContext();
        mContext = context;
    }

    //domainId、userId默认值
    private String domainId = "kitplay.hwcloudlive.com";
    private String userId = "default";
    //默认关闭数据上报功能
    private Boolean isDataReport = true;

    private boolean isLive = false;

    private final String STEP = "step";

    @Override
    public void initPlayer(boolean islive) {
        LogUtils.e("---xb--- initPlayer islive:" + islive);
        isLive = islive;
        mMediaPlayer = new IjkMediaPlayer(mContext, domainId, userId, islive, isDataReport);
        setOptions();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnErrorListener(onErrorListener);
        mMediaPlayer.setOnCompletionListener(onCompletionListener);
        mMediaPlayer.setOnInfoListener(onInfoListener);
        mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
        mMediaPlayer.setOnPreparedListener(onPreparedListener);
        mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        mMediaPlayer.setDynamicBuffer(true);
        mMediaPlayer.setOnNativeInvokeListener(new IjkMediaPlayer.OnNativeInvokeListener() {
            @Override
            public boolean onNativeInvoke(int i, Bundle bundle) {
                return true;
            }
        });

        //sendBroadcastToLogShow(mContext.getString(R.string.init_play));
    }

    @Override
    public void setOptions() {

    }

    @Override
    public void setDataSource(String path, Map<String, String> headers) {
        LogUtils.e("---xb--- setDataSource");
        try {
            Uri uri = Uri.parse(path);
            if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(uri.getScheme())) {
                RawDataSourceProvider rawDataSourceProvider = RawDataSourceProvider.create(mAppContext, uri);
                mMediaPlayer.setDataSource(rawDataSourceProvider);
            } else {
                mMediaPlayer.setDataSource(mAppContext, uri, headers);
            }

        } catch (Exception e) {
            mPlayerEventListener.onError();
        }

        //sendBroadcastToLogShow(mContext.getString(R.string.get_address));

    }

    @Override
    public void setDataSource(AssetFileDescriptor fd) {
        LogUtils.e("---xb--- setDataSource");
        try {
            mMediaPlayer.setDataSource(fd.getFileDescriptor());
        } catch (Exception e) {
            mPlayerEventListener.onError();
        }
    }

    @Override
    public void pause() {
        LogUtils.e("---xb--- pause");
        try {
            mMediaPlayer.pause();
            //sendBroadcastToLogShow(mContext.getString(R.string.pause_play));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        LogUtils.e("---xb--- start");
        mMediaPlayer.start();
        //sendBroadcastToLogShow(mContext.getString(R.string.start_play));
    }

    @Override
    public void stop() {
        LogUtils.e("---xb--- stop");
        try {
            mMediaPlayer.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepareAsync() {
        LogUtils.e("---xb--- prepareAsync");
        try {
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            mPlayerEventListener.onError();
        }
        //sendBroadcastToLogShow(mContext.getString(R.string.prepare_async));
    }

    @Override
    public void reset() {
        LogUtils.e("---xb--- reset");

        mMediaPlayer.reset();
        mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        try {
            mMediaPlayer.setLooping(mIsLooping);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setOptions();
        setEnableMediaCodec(mIsEnableMediaCodec);

        //sendBroadcastToLogShow(mContext.getString(R.string.reset_play));
    }

    @Override
    public boolean isPlaying() {
        LogUtils.e("---xb--- isPlaying");
        PlayDate.setFps(String.format(Locale.US, "%.2f / %.2f", mMediaPlayer.getVideoDecodeFramesPerSecond(), mMediaPlayer.getVideoOutputFramesPerSecond()));
        LogUtils.d("PeriodicReportDate.getFps(): " + PeriodicReportDate.getFps() + " getTcpSpeed(): " + getTcpSpeed() + " getBitRate(): " + getBitRate());
        PlayDate.setSpd(byteToMB(getTcpSpeed()));
        PlayDate.setRes(mMediaPlayer.getVideoHeight() + "x" + mMediaPlayer.getVideoWidth());
        return mMediaPlayer.isPlaying();
    }

    //将字节数转化为MB
    private String byteToMB(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size > kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

    @Override
    public void seekTo(long time) {
        LogUtils.e("LDS ---xb--- seekTo");
        try {
            mMediaPlayer.seekTo((int) time);
            //sendBroadcastToLogShow(mContext.getString(R.string.move_play));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        if (mMediaPlayer != null)
            mMediaPlayer.release();
    }

    @Override
    public long getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getBufferedPercentage() {
        return mBufferedPercent;
    }

    @Override
    public void setSurface(Surface surface) {
        LogUtils.e("---xb--- setSurface");
        mMediaPlayer.setSurface(surface);
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        LogUtils.e("---xb--- setDisplay");
        mMediaPlayer.setDisplay(holder);
    }

    @Override
    public void setVolume(float v1, float v2) {
        mMediaPlayer.setVolume(v1, v2);
    }

    @Override
    public void setLooping(boolean isLooping) {
        this.mIsLooping = isLooping;
        try {
            mMediaPlayer.setLooping(isLooping);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setEnableMediaCodec(boolean isEnable) {
        mIsEnableMediaCodec = isEnable;
        int value = isEnable ? 1 : 0;
        LogUtils.d("value: " + value);
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", value);//开启硬解码
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", value);
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", value);

        //秒开优化 打开则获取不到缓冲时长
        if (isLive) {
            mMediaPlayer.setOption(1, "analyzemaxduration", 100L);
            mMediaPlayer.setOption(1, "probesize", 10240L);
            mMediaPlayer.setOption(1, "flush_packets", 1L);
            mMediaPlayer.setOption(4, "packet-buffering", 0L);
            mMediaPlayer.setOption(4, "framedrop", 1L);
        }

    }

    @Override
    public void setSpeed(float speed) {
        LogUtils.e("---xb--- setSpeed");
        mMediaPlayer.setSpeed(speed);
    }

    @Override
    public long getTcpSpeed() {
        return mMediaPlayer.getTcpSpeed();
    }

//    @Override
//    public Object getProperties(HAGetParam param) {
//        return null;
//    }

    @Override
    public int getVideoWidth() {
        return 0;
    }

    @Override
    public int getVideoHeight() {
        return 0;
    }

    private IMediaPlayer.OnErrorListener onErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int framework_err, int impl_err) {
            LogUtils.v("---xb---l OnErrorListener: framework_err=" + framework_err + "  impl_err=" + impl_err);
            mPlayerEventListener.onError();

            return true;
        }
    };

    private IMediaPlayer.OnCompletionListener onCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            LogUtils.v("---xb---l OnCompletionListener");
            mPlayerEventListener.onCompletion(iMediaPlayer);
            if (iMediaPlayer.getCurrentPosition() < iMediaPlayer.getDuration() - 2000) {//边播边缓存m3u8可能会提前走播放完成步骤
            } else {
                //sendBroadcastToLogShow(mContext.getString(R.string.finish_play));
            }
        }
    };

    private IMediaPlayer.OnInfoListener onInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
            LogUtils.v("---xb---l OnInfoListener: what=" + what + "  extra=" + extra);
            mPlayerEventListener.onInfo(what, extra);
            return true;
        }
    };

    private IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
            LogUtils.v("---xb---l OnBufferingUpdateListener: percent=" + percent);
            mBufferedPercent = percent;
        }
    };


    private IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            mPlayerEventListener.onPrepared();
            LogUtils.e("---xb---l onPrepared");
            //sendBroadcastToLogShow(mContext.getString(R.string.prepared));
        }
    };

    private IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
            LogUtils.v("---xb---l onVideoSizeChangedListener: i=" + i + "  i1=" + i1 + " i2=" + i2 + "  i3=" + i3);
            int videoWidth = iMediaPlayer.getVideoWidth();
            int videoHeight = iMediaPlayer.getVideoHeight();
            if (videoWidth != 0 && videoHeight != 0) {
                mPlayerEventListener.onVideoSizeChanged(videoWidth, videoHeight);
            }
        }
    };

    public String getFps() {
        return String.format(Locale.US, "%.2f / %.2f", mMediaPlayer.getVideoDecodeFramesPerSecond(), mMediaPlayer.getVideoOutputFramesPerSecond());
    }

    public Long getBitRate() {
        return mMediaPlayer.getBitRate();
    }

    public long getVideoCachedBytes() {
        return mMediaPlayer.getVideoCachedBytes();
    }

    public long getVideoCachedDuration() {
        return mMediaPlayer.getVideoCachedDuration();
    }

    public String getDataSource() {
        return mMediaPlayer.getDataSource();
    }

    private void sendBroadcastToLogShow(String step) {
        Intent intent = new Intent("playerLog");
        intent.putExtra(STEP, step);
        mContext.sendBroadcast(intent);
    }

}
