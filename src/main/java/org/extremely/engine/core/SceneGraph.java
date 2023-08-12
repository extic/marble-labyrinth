package org.extremely.engine.core;

import org.extremely.engine.core.components.Camera;

public class SceneGraph {
    private SceneObject root;
    private Camera camera;

    public SceneGraph() {
        root = new SceneObject();
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
}
