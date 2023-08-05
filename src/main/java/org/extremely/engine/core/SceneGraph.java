package org.extremely.engine.core;

public class SceneGraph {
    private SceneObject root;

    public SceneGraph() {
        root = new SceneObject();
    }

    public void add(SceneObject object) {
        root.add(object);
    }

    public SceneObject getRoot() {
        return root;
    }
}
