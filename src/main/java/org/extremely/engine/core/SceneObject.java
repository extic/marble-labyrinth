package org.extremely.engine.core;

import org.extremely.engine.core.components.Camera;
import org.extremely.engine.rendering.RenderingEngine;
import org.extremely.engine.rendering.Shader;

import java.util.ArrayList;
import java.util.List;

public class SceneObject {
    private final String name;
    private final List<SceneObject> children;
    private final List<SceneComponent> components;
    private final SceneObjectTransform transform;
    private SceneObject parent;

    public SceneObject(String name) {
        this.name = name;
        children = new ArrayList<>();
        components = new ArrayList<>();
        transform = new SceneObjectTransform();
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

    public void input() {
        for (SceneComponent component : components) {
            component.input();
        }

        for (SceneObject child : children) {
            child.input();
        }
    }

    public void update(float frameTime) {
        boolean updated = false;
        if (transform.isChanged()) {
            transform.update(parent.transform.getWorldMatrix());
            updated = true;
        }

        for (SceneComponent component : components) {
            component.update(frameTime);
        }

        for (SceneObject child : children) {
            if (updated) {
                child.transform.setChanged(true);
            }
            child.update(frameTime);
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

    public SceneObjectTransform getTransform() {
        return transform;
    }
}
