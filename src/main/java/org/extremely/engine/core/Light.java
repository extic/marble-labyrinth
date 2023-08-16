package org.extremely.engine.core;

import org.joml.Vector3f;

public class Light {
    private final Vector3f position;
    private final Vector3f color;

    public Light(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getColor() {
        return color;
    }
}
