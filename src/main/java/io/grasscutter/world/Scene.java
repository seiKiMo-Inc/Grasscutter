package io.grasscutter.world;

import lombok.Getter;

public class Scene {
    @Getter private final int id;

    public Scene(int id) {
        this.id = id;
    }
}
