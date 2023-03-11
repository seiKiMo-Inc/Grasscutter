package io.grasscutter.world;

import lombok.Getter;
import lombok.Setter;

public class Location extends Position {
    @Getter @Setter
    private transient Scene scene;

    public Location(Scene scene, Position position) {
        this.set(position);

        this.scene = scene;
    }

    public Location(Scene scene, float x, float y) {
        this.set(x, y);

        this.scene = scene;
    }

    public Location(Scene scene, float x, float y, float z) {
        this.set(x, y, z);

        this.scene = scene;
    }

    @Override
    public Location clone() {
        return new Location(this.scene, super.clone());
    }

    @Override
    public String toString() {
        return String.format("%s:%s,%s,%s", this.scene.getId(), this.getX(), this.getY(), this.getZ());
    }
}
