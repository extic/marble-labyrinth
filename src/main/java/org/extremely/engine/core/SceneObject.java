package org.extremely.engine.core;

import org.extremely.engine.core.components.Camera;
import org.extremely.engine.rendering.RenderingEngine;
import org.extremely.engine.rendering.Shader;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class SceneObject {
    private List<SceneObject> children;
    private List<SceneComponent> components;
    private SceneObject parent;
    private Matrix4f transform;

    public SceneObject() {
        children = new ArrayList<>();
        components = new ArrayList<>();
        transform = new Matrix4f().identity();
    }

    public void setParent(SceneObject parent) {
        this.parent = parent;
    }

    public void add(SceneObject object) {
        object.setParent(this);
        children.add(object);
    }

    public void add(SceneComponent component) {
        components.add(component);
        component.setParent(this);
        if (component instanceof Camera camera) {
            Engine.getInstance().getSceneGraph().setCamera(camera);
        }
    }

    public void render(Shader shader, RenderingEngine renderingEngine) {
        for (SceneComponent component : components) {
            component.render(shader, renderingEngine);
        }

        for (SceneObject child : children) {
            child.render(shader, renderingEngine);
        }
    }

    public Matrix4f getTransform() {
        return transform;
    }
}
