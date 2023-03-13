package io.grasscutter.utils.enums.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter public enum SceneLoadState {
    NONE    (0),
    LOADING (1),
    INIT    (2),
    LOADED  (3);

    private final int value;
}
