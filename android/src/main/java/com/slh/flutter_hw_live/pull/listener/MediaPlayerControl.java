package com.slh.flutter_hw_live.pull.listener;

import android.graphics.Bitmap;

public interface MediaPlayerControl {

    void start(boolean islive);

    void pause();

    long getDuration();

    long getCurrentPosition();

    void seekTo(long pos);

    boolean isPlaying();

    int getBufferedPercentage();

    void startFullScreen();

    void stopFullScreen();

    boolean isFullScreen();

    String getTitle();

    void setMute(boolean isMute);

    boolean isMute();

    void setLock(boolean isLocked);

    void setScreenScale(int screenScale);

    void retry();

    void replay(boolean resetPosition);

    void setSpeed(float speed);

    void setDanmuku(boolean isShow);

    void setSubtitle(boolean speed);

    long getTcpSpeed();

    void refresh();

    void setMirrorRotation(boolean enable);

    String getPlayerProp();

    Bitmap doScreenShot();
}