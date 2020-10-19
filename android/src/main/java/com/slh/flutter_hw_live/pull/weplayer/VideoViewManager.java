package com.slh.flutter_hw_live.pull.weplayer;

/**
 * 视频播放器管理器.
 *
 * @author LJ
 * @fileName com.huawei.weplayer.weplayer.VideoViewManager
 */
public class VideoViewManager {

    private WeVideoView mPlayer;

    private VideoViewManager() {
    }

    private static VideoViewManager sInstance;

    public static VideoViewManager instance() {
        if (sInstance == null) {
            synchronized (VideoViewManager.class) {
                if (sInstance == null) {
                    sInstance = new VideoViewManager();
                }
            }
        }
        return sInstance;
    }

    public void setCurrentVideoPlayer(WeVideoView player) {
        mPlayer = player;
    }

    public WeVideoView getCurrentVideoPlayer() {
        return mPlayer;
    }

    public void releaseVideoPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

//    public void stopPlayback() {
//        if (mPlayer != null) mPlayer.stopPlayback();
//    }
//
//    public boolean onBackPressed() {
//        return mPlayer != null && mPlayer.onBackPressed();
//    }
}
