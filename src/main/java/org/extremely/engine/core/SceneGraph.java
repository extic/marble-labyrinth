package org.extremely.engine.core;

import org.extremely.engine.core.components.Camera;

public class SceneGraph {
    private SceneObject root;
    private Camera camera;
    private Light light;

    public SceneGraph() {
        root = new SceneObject("root");
    }

    public void add(SceneObject object) {
        root.add(object);
    }

    public SceneObject getRoot() {
        return root;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Light getLight() {
        return light;
    }

    public void setLight(Light light) {
        this.light = light;
    }
}
