package org.extremely.engine.core;

import org.extremely.engine.rendering.RenderingEngine;
import org.extremely.engine.rendering.Shader;

import java.util.ArrayList;
import java.util.List;

public class SceneObject {
    private List<SceneObject> children;
    private List<SceneComponent> components;

    public SceneObject() {
        children = new ArrayList<>();
        components = new ArrayList<>();
    }

    public void add(SceneObject object) {
        children.add(object);
    }

    public void add(SceneComponent component) {
        components.add(component);
    }

    public void render(Shader shader, RenderingEngine renderingEngine) {
        for(SceneComponent component : components)
            component.render(shader, renderingEngine);

        for(SceneObject child : children)
            child.render(shader, renderingEngine);
    }
}
