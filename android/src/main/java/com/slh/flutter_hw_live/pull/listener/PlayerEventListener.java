package com.slh.flutter_hw_live.pull.listener;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by anber on 2017/12/21.
 */

public interface PlayerEventListener {

    void onError();

    void onCompletion();

    void onCompletion(IMediaPlayer iMediaPlayer);

    void onInfo(int what, int extra);

    void onPrepared();

    void onVideoSizeChanged(int width, int height);

}
