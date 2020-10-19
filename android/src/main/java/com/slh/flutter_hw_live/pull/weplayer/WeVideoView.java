package com.slh.flutter_hw_live.pull.weplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anber.subtitlelibrary.SubtitleReader;
import com.anber.subtitlelibrary.entity.SubtitleLineInfo;
import com.anber.subtitlelibrary.widget.SubtitleView;
import com.slh.flutter_hw_live.pull.listener.OnVideoViewStateChangeListener;
import com.slh.flutter_hw_live.pull.util.LogUtils;
import com.slh.flutter_hw_live.pull.util.PlayerUtils;
import com.slh.flutter_hw_live.pull.widget.ResizeSurfaceView;
import com.slh.flutter_hw_live.pull.widget.ResizeTextureView;

import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * 播放器
 * Created by anber on 2019/2/7.
 */

public class WeVideoView extends BaseWeVideoView {
    protected ResizeSurfaceView mSurfaceView;
    protected ResizeTextureView mTextureView;
    private SubtitleView mSubtitleView;
    protected SurfaceTexture mSurfaceTexture;
    protected FrameLayout mPlayerContainer;
    protected boolean mIsFullScreen;//是否处于全屏状态
    //通过添加和移除这个view来实现隐藏和显示系统navigation bar和status bar，可以避免出现一些奇奇怪怪的问题
    protected View mHideSysBarView;

    /**
     * 字幕事件
     */
    private final int MESSAGE_WHAT_INITDATA = 0;
    private final int MESSAGE_WHAT_UPDATE = 1;

    private SubtitleReader mSubtitleReader;
    private List<SubtitleLineInfo> mSubtitleLineInfos;

    public static final int SCREEN_SCALE_DEFAULT = 0;
    public static final int SCREEN_SCALE_16_9 = 1;
    public static final int SCREEN_SCALE_4_3 = 2;
    public static final int SCREEN_SCALE_MATCH_PARENT = 3;
    public static final int SCREEN_SCALE_ORIGINAL = 4;
    public static final int SCREEN_SCALE_CENTER_CROP = 5;

    protected int mCurrentScreenScale = SCREEN_SCALE_DEFAULT;

    protected int[] mVideoSize = {0, 0};

    private Surface mSurface;

    private static final int FULLSCREEN_FLAGS = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_FULLSCREEN;
    protected boolean mIsTinyScreen;//是否处于小屏状态
    protected int[] mTinyScreenSize = {0, 0};

    public WeVideoView(@NonNull Context context) {
        this(context, null);
    }

    public WeVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化播放器视图
     */
    protected void initView() {
        mPlayerContainer = new FrameLayout(getContext());
        mPlayerContainer.setBackgroundColor(Color.BLACK);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(mPlayerContainer, params);

        mHideSysBarView = new View(getContext());
        mHideSysBarView.setSystemUiVisibility(FULLSCREEN_FLAGS);
    }

    /**
     * 创建播放器实例，设置播放地址及播放器参数
     */
    @Override
    protected void initPlayer(boolean islive) {
        super.initPlayer(islive);
        addDisplay();
    }

    protected void addDisplay() {
        if (mPlayerConfig.usingSurfaceView) {
            addSurfaceView();
        } else {
            addTextureView();
            LogUtils.v("---xxb--- :addTextureView");
        }
    }

    /**
     * 向Controller设置播放状态，用于控制Controller的ui展示
     */
    @Override
    protected void setPlayState(int playState) {
        mCurrentPlayState = playState;
//        if (mVideoController != null)
//            mVideoController.setPlayState(playState);
        LogUtils.d("playState: " + playState);
//        PeriodicReportDate.setPlayerStatus(playState);
        if (mOnVideoViewStateChangeListeners != null) {
            for (OnVideoViewStateChangeListener listener : mOnVideoViewStateChangeListeners) {
                listener.onPlayStateChanged(playState);
            }
        }
    }

    /**
     * 向Controller设置播放器状态，包含全屏状态和非全屏状态
     */
    @Override
    protected void setPlayerState(int playerState) {
        mCurrentPlayerState = playerState;
//        if (mVideoController != null)
//            mVideoController.setPlayerState(playerState);
        if (mOnVideoViewStateChangeListeners != null) {
            for (OnVideoViewStateChangeListener listener : mOnVideoViewStateChangeListeners) {
                listener.onPlayerStateChanged(playerState);
            }
        }
    }

    @Override
    protected void startPlay(boolean islive) {
        if (mPlayerConfig.addToPlayerManager) {
            VideoViewManager.instance().releaseVideoPlayer();
            VideoViewManager.instance().setCurrentVideoPlayer(this);
        }
//        if (checkNetwork()) return;
//        addSubtitleView();
//        new LoadSubtitleAsync().execute("");
        super.startPlay(islive);
    }

    /**
     * 添加SurfaceView
     */
    private void addSurfaceView() {
        mPlayerContainer.removeView(mSurfaceView);
        mSurfaceView = new ResizeSurfaceView(getContext());
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mMediaPlayer != null) {
                    mMediaPlayer.setDisplay(holder);
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
        surfaceHolder.setFormat(PixelFormat.RGBA_8888);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        mPlayerContainer.addView(mSurfaceView, 0, params);
    }

    /**
     * 添加TextureView
     */
    private void addTextureView() {
        mPlayerContainer.removeView(mTextureView);
        mSurfaceTexture = null;
        mTextureView = new ResizeTextureView(getContext());
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                if (mSurfaceTexture != null) {
                    mTextureView.setSurfaceTexture(mSurfaceTexture);
                } else {
                    mSurfaceTexture = surfaceTexture;
                    mMediaPlayer.setSurface(new Surface(surfaceTexture));
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return mSurfaceTexture == null;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        mPlayerContainer.addView(mTextureView, 0, params);
    }

//    /**
//     * 添加字幕view
//     */
//    private void addSubtitleView() {
//        if (mSubtitleView != null) {
//            mPlayerContainer.removeView(mSubtitleView);
//        }
//        mSubtitleView = new SubtitleView(getContext());
//        mSubtitleView.setGravity(Gravity.CENTER);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
//        mPlayerContainer.addView(mSubtitleView, params);
//    }

    protected boolean checkNetwork() {
        if (PlayerUtils.getNetworkType(getContext()) == PlayerUtils.NETWORK_MOBILE
                && !IS_PLAY_ON_MOBILE_NETWORK) {
//            if (mVideoController != null) {
//                mVideoController.showStatusView();
//            }
            return true;
        }
        return false;
    }

    @Override
    public void release() {
        super.release();
        mPlayerContainer.removeView(mTextureView);
        mPlayerContainer.removeView(mSurfaceView);
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        mCurrentScreenScale = SCREEN_SCALE_DEFAULT;
    }

//    /**
//     * 进入全屏
//     */
//    @Override
//    public void startFullScreen() {
//        if (mVideoController == null) return;
//        Activity activity = PlayerUtils.scanForActivity(mVideoController.getContext());
//        if (activity == null) return;
//        if (mIsFullScreen) return;
//        PlayerUtils.hideActionBar(mVideoController.getContext());
//        this.addView(mHideSysBarView);
//        this.removeView(mPlayerContainer);
//        ViewGroup contentView = activity
//                .findViewById(android.R.id.content);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        contentView.addView(mPlayerContainer, params);
//        mOrientationEventListener.enable();
//        mIsFullScreen = true;
//        setPlayerState(PLAYER_FULL_SCREEN);
//    }

//    /**
//     * 退出全屏
//     */
//    @Override
//    public void stopFullScreen() {
//        if (mVideoController == null) return;
//        Activity activity = PlayerUtils.scanForActivity(mVideoController.getContext());
//        if (activity == null) return;
//        if (!mIsFullScreen) return;
//        if (!mPlayerConfig.mAutoRotate) mOrientationEventListener.disable();
//        PlayerUtils.showActionBar(mVideoController.getContext());
//        this.removeView(mHideSysBarView);
//        ViewGroup contentView = activity
//                .findViewById(android.R.id.content);
//        contentView.removeView(mPlayerContainer);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        this.addView(mPlayerContainer, params);
//        this.requestFocus();
//        mIsFullScreen = false;
//        setPlayerState(PLAYER_NORMAL);
//    }

    /**
     * 判断是否处于全屏状态
     */
    public boolean isFullScreen() {
        return mIsFullScreen;
    }

    /**
     * 开启小屏
     */
//    public void startTinyScreen() {
//        if (mIsTinyScreen) return;
//        Activity activity = PlayerUtils.scanForActivity(getContext());
//        if (activity == null) return;
//        mOrientationEventListener.disable();
//        this.removeView(mPlayerContainer);
//        ViewGroup contentView = activity.findViewById(android.R.id.content);
//        int width = mTinyScreenSize[0];
//        if (width <= 0) {
//            width = PlayerUtils.getScreenWidth(activity) / 2;
//        }
//
//        int height = mTinyScreenSize[1];
//        if (height <= 0) {
//            height = width * 9 / 16;
//        }
//
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
//        params.gravity = Gravity.BOTTOM | Gravity.END;
//        contentView.addView(mPlayerContainer, params);
//        mIsTinyScreen = true;
//        setPlayerState(PLAYER_TINY_SCREEN);
//    }

    /**
     * 退出小屏
     */
//    public void stopTinyScreen() {
//        if (!mIsTinyScreen) return;
//
//        Activity activity = PlayerUtils.scanForActivity(getContext());
//        if (activity == null) return;
//
//        ViewGroup contentView = activity.findViewById(android.R.id.content);
//        contentView.removeView(mPlayerContainer);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        this.addView(mPlayerContainer, params);
//        mOrientationEventListener.enable();
//
//        mIsTinyScreen = false;
//        setPlayerState(PLAYER_NORMAL);
//    }

    public boolean isTinyScreen() {
        return mIsTinyScreen;
    }

    /**
     * 重试
     */

    public void retry() {
        addDisplay();
       // setVideoController(mVideoController);
        startPrepare(true);
    }

//    /**
//     * 字幕
//     */
//    @Override
//    public void setSubtitle(boolean mIsSubtiltle) {
//        if (mIsSubtiltle) {
//            mHandler.sendEmptyMessage(MESSAGE_WHAT_UPDATE);
//            mSubtitleReader.setOffset(-7 * 1000);
//        } else {
//            mHandler.removeMessages(MESSAGE_WHAT_UPDATE);
//            mSubtitleView.setText("");
//        }
//    }

//    @Override
//    public void setDanmuku(boolean isShow) {
//    }

    @Override
    public void onInfo(int what, int extra) {
        LogUtils.d("onInfowhat: " + what);
        super.onInfo(what, extra);
        switch (what) {
//            case weMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
//                if (mTextureView != null)
//                    mTextureView.setRotation(extra);
//                break;
        }
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        if (iMediaPlayer.getCurrentPosition() < iMediaPlayer.getDuration() - 2000) {//边播边缓存m3u8可能会提前走播放完成步骤
            seekTo(iMediaPlayer.getCurrentPosition());
            new Thread (new Runnable(){
                public void run(){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    resume();
                }
            }).start();
        } else {
            super.onCompletion();
        }

    }

    @Override
    public void onVideoSizeChanged(int videoWidth, int videoHeight) {
        mVideoSize[0] = videoWidth;
        mVideoSize[1] = videoHeight;
        if (mPlayerConfig.usingSurfaceView) {
            mSurfaceView.setScreenScale(mCurrentScreenScale);
            mSurfaceView.setVideoSize(videoWidth, videoHeight);
        } else {
            mTextureView.setScreenScale(mCurrentScreenScale);
            mTextureView.setVideoSize(videoWidth, videoHeight);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //重新获得焦点时保持全屏状态
        if (hasFocus && mIsFullScreen) {
            mHideSysBarView.setSystemUiVisibility(FULLSCREEN_FLAGS);
        }

        if (isInPlaybackState() && mIsFullScreen) {
            if (hasFocus) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       // mOrientationEventListener.enable();
                    }
                }, 800);
            } else {
               // mOrientationEventListener.disable();
            }
        }
    }

    /**
     * 重新播放
     *
     * @param resetPosition 是否从头开始播放
     */

    public void replay(boolean resetPosition) {
        if (resetPosition) {
            mCurrentPosition = 0;
        }
        addDisplay();
        startPrepare(true);
    }

//    /**
//     * 设置控制器
//     */
//    public void setVideoController(@Nullable BaseVideoController mediaController) {
//        mPlayerContainer.removeView(mVideoController);
//        mVideoController = mediaController;
//        if (mediaController != null) {
//            mediaController.setMediaPlayer(this);
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT);
//            mPlayerContainer.addView(mVideoController, params);
//        }
//    }

//    /**
//     * 改变返回键逻辑，用于activity
//     */
//    public boolean onBackPressed() {
//        return mVideoController != null && mVideoController.onBackPressed();
//    }

    /**
     * 设置视频比例
     */
    public void setScreenScale(int screenScale) {
        this.mCurrentScreenScale = screenScale;
        if (mSurfaceView != null) {
            mSurfaceView.setScreenScale(screenScale);
        } else if (mTextureView != null) {
            mTextureView.setScreenScale(screenScale);
        }
    }

    /**
     * 设置镜像旋转，暂不支持SurfaceView
     */
    public void setMirrorRotation(boolean enable) {
        if (mTextureView != null) {
            mTextureView.setScaleX(enable ? -1 : 1);
        }
    }

    /**
     * 截图，暂不支持SurfaceView
     */
    public Bitmap doScreenShot() {
        if (mTextureView != null) {
            return mTextureView.getBitmap();
        }
        return null;
    }

    /**
     * 获取视频宽高,其中width: mVideoSize[0], height: mVideoSize[1]
     */
    public int[] getVideoSize() {
        return mVideoSize;
    }

    /**
     * 旋转视频画面
     *
     * @param rotation 角度
     */
    @Override
    public void setRotation(float rotation) {
        if (mTextureView != null) {
            mTextureView.setRotation(rotation);
            mTextureView.requestLayout();
        }

        if (mSurfaceView != null) {
            mSurfaceView.setRotation(rotation);
            mSurfaceView.requestLayout();
        }
    }

    /**
     * 设置小屏的宽高
     *
     * @param tinyScreenSize 其中tinyScreenSize[0]是宽，tinyScreenSize[1]是高
     */
//    public void setTinyScreenSize(int[] tinyScreenSize) {
//        this.mTinyScreenSize = tinyScreenSize;
//    }

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MESSAGE_WHAT_INITDATA:
//                    LogUtils.v("---xxb--- :load suc");
//                    break;
//
//                case MESSAGE_WHAT_UPDATE:
//                    if (isPlaying()) {
//                        long progress = getCurrentPosition();
//                        int lineNumber = SubtitleUtil.getLineNumber(mSubtitleLineInfos, progress, mSubtitleReader.getPlayOffset());
//                        if (lineNumber == -1) {
//                            mSubtitleView.setText("");
//                        } else {
//                            SubtitleLineInfo subtitleLineInfo = mSubtitleLineInfos.get(lineNumber);
//                            mSubtitleView.setText(Html.fromHtml(subtitleLineInfo.getSubtitleHtml()));
//                            //mSubtitleView.setText(subtitleLineInfo.getSubtitleText());
//                        }
//                    }
//                    sendEmptyMessageDelayed(MESSAGE_WHAT_UPDATE, 500);
//                    break;
//            }
//        }
//    };

//    class LoadSubtitleAsync extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            try {
//                InputStream inputStream = getResources().openRawResource(R.raw.shame_ass);
//                AssSubtitleFileReader assSubtitleFileReader = new AssSubtitleFileReader();
//                SubtitleInfo subtitleInfo = assSubtitleFileReader.readInputStream(inputStream);
//
//                mSubtitleReader = new SubtitleReader();
//                mSubtitleReader.setSubtitleInfo(subtitleInfo);
//                mSubtitleLineInfos = subtitleInfo.getSubtitleLineInfos();
//
//                mHandler.sendEmptyMessage(MESSAGE_WHAT_INITDATA);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
}
