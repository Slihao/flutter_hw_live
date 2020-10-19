package com.slh.flutter_hw_live.pull.weplayer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anber.m3u8Cache.M3U8Cache;
import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.slh.flutter_hw_live.pull.listener.OnVideoViewStateChangeListener;
import com.slh.flutter_hw_live.pull.listener.PlayerEventListener;
import com.slh.flutter_hw_live.pull.util.Data;
import com.slh.flutter_hw_live.pull.util.LogUtils;
import com.slh.flutter_hw_live.pull.util.ProgressUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * 播放器
 * Created by anber on 2019/2/7.
 */

public abstract class BaseWeVideoView extends FrameLayout implements PlayerEventListener {

    protected AbstractPlayer mMediaPlayer;//播放器
    //    @Nullable
//    protected BaseVideoController mVideoController;//控制器
    protected int mBufferedPercentage;//缓冲百分比
    protected boolean mIsMute;//是否静音

    public static boolean isM3u8 = false;//是否是播放m3u8格式的视频
    public static boolean isM3u8Downed = false;//m3u8格式的视频是否已经下载完毕

    protected String mCurrentUrl;//当前播放视频的地址
    protected Map<String, String> mHeaders;//当前视频地址的请求头
    protected AssetFileDescriptor mAssetFileDescriptor;//assets文件
    protected long mCurrentPosition;//当前正在播放视频的位置
    protected String mCurrentTitle = "";//当前正在播放视频的标题


    //播放器的各种状态
    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_PLAYBACK_COMPLETED = 5;
    public static final int STATE_BUFFERING = 6;
    public static final int STATE_BUFFERED = 7;
    protected int mCurrentPlayState = STATE_IDLE;//当前播放器的状态

    public static final int PLAYER_NORMAL = 10;        // 普通播放器
    public static final int PLAYER_FULL_SCREEN = 11;   // 全屏播放器
    public static final int PLAYER_TINY_SCREEN = 12;   // 小屏播放器
    protected int mCurrentPlayerState = PLAYER_NORMAL;

    protected AudioManager mAudioManager;//系统音频管理器
    @Nullable
    protected AudioFocusHelper mAudioFocusHelper;

    protected int mCurrentOrientation = 0;
    protected static final int PORTRAIT = 1;
    protected static final int LANDSCAPE = 2;
    protected static final int REVERSE_LANDSCAPE = 3;

    protected boolean mIsLockFullScreen;//是否锁定屏幕
    protected PlayerConfig mPlayerConfig;//播放器配置
    private HttpProxyCacheServer mCacheServer;

    public static boolean IS_PLAY_ON_MOBILE_NETWORK = false;//记录是否在移动网络下播放视频

    protected List<OnVideoViewStateChangeListener> mOnVideoViewStateChangeListeners;


//    /**
//     * 加速度传感器监听
//     */
//    protected OrientationEventListener mOrientationEventListener = new OrientationEventListener(getContext()) { // 加速度传感器监听，用于自动旋转屏幕
//        @Override
//        public void onOrientationChanged(int orientation) {
//            if (mVideoController == null) return;
//            Activity activity = PlayerUtils.scanForActivity(mVideoController.getContext());
//            if (activity == null) return;
//            if (orientation >= 340) { //屏幕顶部朝上
//                onOrientationPortrait(activity);
//            } else if (orientation >= 260 && orientation <= 280) { //屏幕左边朝上
//                onOrientationLandscape(activity);
//            } else if (orientation >= 70 && orientation <= 90) { //屏幕右边朝上
//                onOrientationReverseLandscape(activity);
//            }
//        }
//    };

//    /**
//     * 竖屏
//     */
//    protected void onOrientationPortrait(Activity activity) {
//        if (mIsLockFullScreen || !mPlayerConfig.mAutoRotate || mCurrentOrientation == PORTRAIT)
//            return;
//        if ((mCurrentOrientation == LANDSCAPE || mCurrentOrientation == REVERSE_LANDSCAPE) && !isFullScreen()) {
//            mCurrentOrientation = PORTRAIT;
//            return;
//        }
//        mCurrentOrientation = PORTRAIT;
//        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        stopFullScreen();
//    }
//
//    /**
//     * 横屏
//     */
//    protected void onOrientationLandscape(Activity activity) {
//        if (mCurrentOrientation == LANDSCAPE) return;
//        if (mCurrentOrientation == PORTRAIT && isFullScreen()) {
//            mCurrentOrientation = LANDSCAPE;
//            return;
//        }
//        mCurrentOrientation = LANDSCAPE;
//        if (!isFullScreen()) {
//            startFullScreen();
//        }
//        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//    }
//
//    /**
//     * 反向横屏
//     */
//    protected void onOrientationReverseLandscape(Activity activity) {
//        if (mCurrentOrientation == REVERSE_LANDSCAPE) return;
//        if (mCurrentOrientation == PORTRAIT && isFullScreen()) {
//            mCurrentOrientation = REVERSE_LANDSCAPE;
//            return;
//        }
//        mCurrentOrientation = REVERSE_LANDSCAPE;
//        if (!isFullScreen()) {
//            startFullScreen();
//        }
//        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
//    }

    public BaseWeVideoView(@NonNull Context context) {
        this(context, null);
    }


    public BaseWeVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseWeVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPlayerConfig = new PlayerConfig.Builder().build();
    }

    /**
     * 初始化播放器
     */
    protected void initPlayer(boolean islive) {
        if (mMediaPlayer == null) {
            if (mPlayerConfig.mAbstractPlayer != null) {
                mMediaPlayer = mPlayerConfig.mAbstractPlayer;
            } else {
                mMediaPlayer = new vPlayer(getContext());
            }
            mMediaPlayer.bindVideoView(this);
            mMediaPlayer.initPlayer(islive);
            mMediaPlayer.setEnableMediaCodec(mPlayerConfig.enableMediaCodec);
            mMediaPlayer.setLooping(mPlayerConfig.isLooping);
        }
    }

//    protected abstract void setPlayState(int playState);
//
//    protected abstract void setPlayerState(int playerState);

    /**
     * 向Controller设置播放状态，用于控制Controller的ui展示
     */
    protected void setPlayState(int playState) {
        mCurrentPlayState = playState;
//        if (mVideoController != null)
//            mVideoController.setPlayState(playState);
        if (mOnVideoViewStateChangeListeners != null) {
            for (int i = 0, z = mOnVideoViewStateChangeListeners.size(); i < z; i++) {
                OnVideoViewStateChangeListener listener = mOnVideoViewStateChangeListeners.get(i);
                if (listener != null) {
                    listener.onPlayStateChanged(playState);
                }
            }
        }
    }

    /**
     * 向Controller设置播放器状态，包含全屏状态和非全屏状态
     */
    protected void setPlayerState(int playerState) {
        mCurrentPlayerState = playerState;
//        if (mVideoController != null)
//            mVideoController.setPlayerState(playerState);
        if (mOnVideoViewStateChangeListeners != null) {
            for (int i = 0, z = mOnVideoViewStateChangeListeners.size(); i < z; i++) {
                OnVideoViewStateChangeListener listener = mOnVideoViewStateChangeListeners.get(i);
                if (listener != null) {
                    listener.onPlayerStateChanged(playerState);
                }
            }
        }
    }

    /**
     * 开始准备播放（直接播放）
     */
    protected void startPrepare(boolean needReset) {

        LogUtils.d("startPrepare: " + "mPlayerConfig.isCache: " + mPlayerConfig.isCache + " url: " + mCurrentUrl);
        if (TextUtils.isEmpty(mCurrentUrl) && mAssetFileDescriptor == null) return;
        String url = mCurrentUrl;

        //mCacheServer = getCacheServer();
        if (mCacheServer == null) {
            LogUtils.d("onPlayStopped mCacheServer == null");
        }
        LogUtils.d("onPlayStopped registerCacheListener");
      //  VideoCacheManager.getInstance(getContext().getApplicationContext()).registerCacheListener(url);
        if (needReset) mMediaPlayer.reset();
        if (mAssetFileDescriptor != null) {
            mMediaPlayer.setDataSource(mAssetFileDescriptor);
        }  else {
            if (mPlayerConfig.autoContact && (url.startsWith("http:") || url.startsWith("rtmp://"))) {
                url = "ijklivehook:" + url;
            }
            LogUtils.v("---xbb--- startPrepare url=" + url);
            mMediaPlayer.setDataSource(url, mHeaders);
        }
//        else if (mPlayerConfig.isCache && !url.startsWith("file://") && !url.contains("/storage/emulated/0/") && url.contains(".mp4")) {
//            LogUtils.v("---xbb--- getCacheServer start");
//            String proxyPath = VideoCacheManager.getInstance(getContext().getApplicationContext()).getProxyUrl(url);
//            if (!proxyPath.startsWith("file://") && mPlayerConfig.autoContact) {//缓存好的视频不需要断网重连
//                proxyPath = "ijkhttphook:" + proxyPath;
//            }
////            String proxyPath =url;
//            if (mCacheServer.isCached(url)) {
//                mBufferedPercentage = 100;
//                LogUtils.v("---xbb--- mBufferedPercentage=" + mBufferedPercentage);
//            }
//            LogUtils.d("proxyPath: " + proxyPath + " mHeaders: " + mHeaders);
//            mMediaPlayer.setDataSource(proxyPath, mHeaders);
//
//        }

        mMediaPlayer.prepareAsync();
        setPlayState(STATE_PREPARING);
        //setPlayerState(isFullScreen() ? PLAYER_FULL_SCREEN : PLAYER_NORMAL);
    }

//    private HttpProxyCacheServer getCacheServer() {
//        return VideoCacheManager.getProxy(getContext().getApplicationContext());
//        return  null;
//    }

    /**
     * 开始播放
     */
    public void start(boolean islive) {
        if (mCurrentPlayState == STATE_IDLE) {
            startPlay(islive);
        } else if (isInPlaybackState()) {
            startInPlaybackState();
        }
        setKeepScreenOn(true);
        if (mAudioFocusHelper != null)
            mAudioFocusHelper.requestFocus();
    }

    /**
     * 第一次播放
     */
    protected void startPlay(boolean islive) {
        if (!mPlayerConfig.disableAudioFocus) {
            mAudioManager = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            mAudioFocusHelper = new AudioFocusHelper();
        }
        if (mPlayerConfig.savingProgress) {
            mCurrentPosition = ProgressUtil.getSavedProgress(mCurrentUrl);
        }
//        if (mPlayerConfig.mAutoRotate)
//            mOrientationEventListener.enable();
        initPlayer(islive);
        startPrepare(false);
    }

    /**
     * 播放状态下开始播放
     */
    protected void startInPlaybackState() {
        mMediaPlayer.start();
        setPlayState(STATE_PLAYING);
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (isPlaying()) {
            mMediaPlayer.pause();
            setPlayState(STATE_PAUSED);
            setKeepScreenOn(false);
            if (mAudioFocusHelper != null)
                mAudioFocusHelper.abandonFocus();
        }
    }

    /**
     * 继续播放
     */
    public void resume() {
        if (isInPlaybackState()
                && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
            setPlayState(STATE_PLAYING);
            if (mAudioFocusHelper != null)
                mAudioFocusHelper.requestFocus();
            setKeepScreenOn(true);
        }
    }

    /**
     * 停止播放
     */
    public void stopPlayback() {
        if (mPlayerConfig.savingProgress && isInPlaybackState())
            ProgressUtil.saveProgress(mCurrentUrl, mCurrentPosition);
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            setPlayState(STATE_IDLE);
            if (mAudioFocusHelper != null)
                mAudioFocusHelper.abandonFocus();
            setKeepScreenOn(false);
        }
        onPlayStopped();
    }

    /**
     * 释放播放器
     */
    public void release() {
        if (mPlayerConfig.savingProgress && isInPlaybackState())
            ProgressUtil.saveProgress(mCurrentUrl, mCurrentPosition);
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            setPlayState(STATE_IDLE);
            if (mAudioFocusHelper != null)
                mAudioFocusHelper.abandonFocus();
            setKeepScreenOn(false);
        }
        onPlayStopped();
    }

    private void onPlayStopped() {
        LogUtils.d("onPlayStopped");
//        if (mVideoController != null) mVideoController.hideStatusView();
//        mOrientationEventListener.disable();
        if (mCacheServer != null) {
            LogUtils.d("onPlayStopped unregisterCacheListener");
//            mCacheServer.unregisterCacheListener(cacheListener,mCurrentUrl);
            //VideoCacheManager.getInstance(getContext().getApplicationContext()).unregisterCacheListener();
        }

        mIsLockFullScreen = false;
        mCurrentPosition = 0;
    }

    /**
     * 监听播放状态变化
     */
    public void addOnVideoViewStateChangeListener(@NonNull OnVideoViewStateChangeListener listener) {
        if (mOnVideoViewStateChangeListeners == null) {
            mOnVideoViewStateChangeListeners = new ArrayList<>();
        }
        mOnVideoViewStateChangeListeners.add(listener);
    }

    /**
     * 移除播放状态监听
     */
    public void removeOnVideoViewStateChangeListener(@NonNull OnVideoViewStateChangeListener listener) {
        if (mOnVideoViewStateChangeListeners != null) {
            mOnVideoViewStateChangeListeners.remove(listener);
        }
    }

    /**
     * 移除所有播放状态监听
     */
    public void clearOnVideoViewStateChangeListeners() {
        if (mOnVideoViewStateChangeListeners != null) {
            mOnVideoViewStateChangeListeners.clear();
        }
    }

    /**
     * 是否处于播放状态
     */
    protected boolean isInPlaybackState() {
        return (mMediaPlayer != null
                && mCurrentPlayState != STATE_ERROR
                && mCurrentPlayState != STATE_IDLE
                && mCurrentPlayState != STATE_PREPARING
                && mCurrentPlayState != STATE_PLAYBACK_COMPLETED);
    }

    /**
     * 获取视频总时长
     */
    public long getDuration() {
        if (isInPlaybackState()) {
            LogUtils.v("getDuration=" + mMediaPlayer.getDuration());
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    /**
     * 获取当前播放的位置
     */
    public long getCurrentPosition() {
        if (isInPlaybackState()) {
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
            return mCurrentPosition;
        }
        return 0;
    }

    /**
     * 调整播放进度
     */
    public void seekTo(final long pos) {

        if (isInPlaybackState()) {
            if (isM3u8 && !isM3u8Downed) {
                M3U8Cache.getInstance().seek(pos);
                LogUtils.e("pos: " + pos);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mMediaPlayer.seekTo(pos);
                    }
                }, 1000);//延时1s执行

            } else {
                mMediaPlayer.seekTo(pos);
            }
        }

    }

    /**
     * 是否处于播放状态
     */
    public boolean isPlaying() {
        return isInPlaybackState() && mMediaPlayer.isPlaying();
    }

    /**
     * 获取当前缓冲百分比
     */
    public int getBufferedPercentage() {
        return mMediaPlayer != null ? mMediaPlayer.getBufferedPercentage() : 0;
    }

    /**
     * 设置静音
     */
    public void setMute(boolean isMute) {
        if (mMediaPlayer != null) {
            this.mIsMute = isMute;
            float volume = isMute ? 0.0f : 1.0f;
            mMediaPlayer.setVolume(volume, volume);
        }
    }

    /**
     * 是否处于静音状态
     */
    public boolean isMute() {
        return mIsMute;
    }

    /**
     * 设置controller是否处于锁定状态
     */
    public void setLock(boolean isLocked) {
        this.mIsLockFullScreen = isLocked;
    }

    /**
     * 获取当前播放视频的标题
     */
    public String getTitle() {
        return mCurrentTitle;
    }

    /**
     * 缓存监听
     */
    private CacheListener cacheListener = new CacheListener() {
        @Override
        public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
            mBufferedPercentage = percentsAvailable;
            LogUtils.v("percentsAvail ---xb--- mBufferedPercentage=" + mBufferedPercentage + " url: " + url);

        }
    };

    /**
     * 视频播放出错回调
     */
    @Override
    public void onError() {
        setPlayState(STATE_ERROR);
    }

    /**
     * 视频播放完成回调
     */
    @Override
    public void onCompletion() {
        setPlayState(STATE_PLAYBACK_COMPLETED);
        setKeepScreenOn(false);
        mCurrentPosition = 0;

    }

    @Override
    public void onInfo(int what, int extra) {
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                setPlayState(STATE_BUFFERING);
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                setPlayState(STATE_BUFFERED);
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: // 视频开始渲染
                setPlayState(STATE_PLAYING);
                if (getWindowVisibility() != VISIBLE) pause();
                break;
        }
    }

    /**
     * 视频缓冲完毕，准备开始播放时回调
     */
    @Override
    public void onPrepared() {
        LogUtils.v("---xb--- onPrepared setPlayState(STATE_PREPARED)");
        setPlayState(STATE_PREPARED);
        if (mCurrentPosition > 0) {
            seekTo(mCurrentPosition);
        }
    }

    public void setPlayerConfig(PlayerConfig config) {
        this.mPlayerConfig = config;
    }

    /**
     * 获取当前播放器的状态
     */
    public int getCurrentPlayerState() {
        return mCurrentPlayerState;
    }

    /**
     * 获取当前的播放状态
     */
    public int getCurrentPlayState() {
        return mCurrentPlayState;
    }


    /**
     * 获取缓冲速度
     */
    public long getTcpSpeed() {
        return mMediaPlayer.getTcpSpeed();
    }

    /**
     * 设置播放速度
     */
    public void setSpeed(float speed) {
        if (isInPlaybackState()) {
            mMediaPlayer.setSpeed(speed);
        }
    }

    /**
     * 重新播放
     */
    public void refresh() {
        mCurrentPosition = 0;
        //retry();
    }

    /**
     * 设置视频地址
     */
    public void setUrl(String url) {
        this.mCurrentUrl = url;
    }

    /**
     * 设置包含请求头信息的视频地址
     *
     * @param url     视频地址
     * @param headers 请求头
     */
    public void setUrl(String url, Map<String, String> headers) {
        mCurrentUrl = url;
        mHeaders = headers;
    }

    /**
     * 用于播放assets里面的视频文件
     */
    public void setAssetFileDescriptor(AssetFileDescriptor fd) {
        this.mAssetFileDescriptor = fd;
    }

    /**
     * 一开始播放就seek到预先设置好的位置
     */
    public void skipPositionWhenPlay(int position) {
        this.mCurrentPosition = position;
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        if (title != null) {
            this.mCurrentTitle = title;
        }
    }

    /**
     * 设置音量 0.0f-1.0f 之间
     *
     * @param v1 左声道音量
     * @param v2 右声道音量
     */
    public void setVolume(float v1, float v2) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(v1, v2);
        }
    }

    /**
     * 音频焦点改变监听
     */
    private class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {
        private boolean startRequested = false;
        private boolean pausedForLoss = false;
        private int currentFocus = 0;

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (currentFocus == focusChange) {
                return;
            }

            currentFocus = focusChange;
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN://获得焦点
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT://暂时获得焦点
                    if (startRequested || pausedForLoss) {
                        start(Data.getType());
                        startRequested = false;
                        pausedForLoss = false;
                    }
                    if (mMediaPlayer != null && !mIsMute)//恢复音量
                        mMediaPlayer.setVolume(1.0f, 1.0f);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS://焦点丢失
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://焦点暂时丢失
                    if (isPlaying()) {
                        pausedForLoss = true;
                        pause();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://此时需降低音量
                    if (mMediaPlayer != null && isPlaying() && !mIsMute) {
                        mMediaPlayer.setVolume(0.1f, 0.1f);
                    }
                    break;
            }
        }

        /**
         * Requests to obtain the audio focus
         *
         * @return True if the focus was granted
         */
        boolean requestFocus() {
            if (currentFocus == AudioManager.AUDIOFOCUS_GAIN) {
                return true;
            }

            if (mAudioManager == null) {
                return false;
            }

            int status = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status) {
                currentFocus = AudioManager.AUDIOFOCUS_GAIN;
                return true;
            }

            startRequested = true;
            return false;
        }

        /**
         * Requests the system to drop the audio focus
         *
         * @return True if the focus was lost
         */
        boolean abandonFocus() {

            if (mAudioManager == null) {
                return false;
            }

            startRequested = false;
            int status = mAudioManager.abandonAudioFocus(this);
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status;
        }
    }

    public AbstractPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }

    /**
     * 获取帧率
     */
    public String getPlayerProp() {
        String prop = "";
        if (mMediaPlayer != null && !((vPlayer) mMediaPlayer).getFps().equals("")) {
            prop = "FPS: " + ((vPlayer) mMediaPlayer).getFps() + "\n"/*+"BitRate: "+((vPlayer) mMediaPlayer).getBitRate()+"\n"*/
                    + "TcpSpeed: " + byteToMB(((vPlayer) mMediaPlayer).getTcpSpeed()) + "/s\n" + "VideoCachedBytes: " + byteToMB(((vPlayer) mMediaPlayer).getVideoCachedBytes()) + "\n"
                    + "VideoCachedDuration: " + msToMS(((vPlayer) mMediaPlayer).getVideoCachedDuration()) + "\n" + "" + "URL: " + ((vPlayer) mMediaPlayer).getDataSource();
        } else {
            prop = "";
        }

        return prop;
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

    private String msToMS(long time) {

        String min = (time / (1000 * 60)) + "";
        String second = (time % (1000 * 60) / 1000) + "";
        if (min.length() < 2) {
            min = 0 + min;
        }
        if (second.length() < 2) {
            second = 0 + second;
        }
        return min + ":" + second;

    }

    public static void setIsM3u8(boolean isM3u8) {
        // DefinitionWeVideoView.isM3u8 = isM3u8;
    }

    public static void setIsM3u8Downed(boolean isM3u8Downed) {
        BaseWeVideoView.isM3u8Downed = isM3u8Downed;
    }

    public static boolean getIsM3u8Downed() {
        return isM3u8Downed;
    }
}
