package org.extremely.engine.core;

import org.extremely.engine.rendering.RenderingEngine;
import org.extremely.engine.rendering.Shader;
import org.joml.Matrix4f;

public class SceneComponent {
    private SceneObject parent;

    public void setParent(SceneObject parent) {
        this.parent = parent;
    }

    public void render(Shader shader, RenderingEngine renderingEngine) {}

    public Matrix4f getTransform() {
        return parent.getTransform();
    }
}
