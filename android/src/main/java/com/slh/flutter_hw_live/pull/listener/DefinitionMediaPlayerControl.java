package com.slh.flutter_hw_live.pull.listener;

import java.util.LinkedHashMap;

/**
 * 清晰度
 * Created by anber on 2017/12/25.
 */

public interface DefinitionMediaPlayerControl extends MediaPlayerControl {
    LinkedHashMap<String, String> getDefinitionData();

    void switchDefinition(String definition);
}
