package org.extremely.engine.core;

import org.extremely.engine.rendering.RenderingEngine;
import org.extremely.engine.rendering.Shader;

public class SceneComponent {
    private SceneObject parent;

    public void setParent(SceneObject parent) {
        this.parent = parent;
    }

    public void input() {
    }

    public void update(float frameTime) {
    }

    public void render(Shader shader, RenderingEngine renderingEngine) {}

    public SceneObjectTransform getTransform() {
        return parent.getTransform();
    }
}
