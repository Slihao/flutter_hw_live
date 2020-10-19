package com.slh.flutter_hw_live.pull.listener;

public interface OnVideoViewStateChangeListener {
    void onPlayerStateChanged(int playerState);

    void onPlayStateChanged(int playState);
}
