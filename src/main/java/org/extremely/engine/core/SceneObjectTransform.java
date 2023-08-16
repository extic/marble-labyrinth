package org.extremely.engine.core;

import org.joml.Matrix4f;

public class SceneObjectTransform {
    private final Matrix4f local;
    private final Matrix4f world;
    private boolean changed;

    public SceneObjectTransform() {
        local = new Matrix4f().identity();
        world = new Matrix4f().identity();
        changed = false;
    }

    public Matrix4f getLocalMatrix() {
        return local;
    }

    public Matrix4f getWorldMatrix() {
        return world;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public void update(Matrix4f parentWorld) {
        parentWorld.mul(local, world);
    }
}
