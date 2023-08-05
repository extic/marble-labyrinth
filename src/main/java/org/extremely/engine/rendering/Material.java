package org.extremely.engine.rendering;

import java.util.HashMap;
import java.util.Map;

public class Material {
    private Map<String, Texture> textureMap;

    public Material(Texture diffuse) {
        textureMap = new HashMap<String, Texture>();

        addTexture("diffuse", diffuse);
    }

    public void addTexture(String name, Texture texture) {
        textureMap.put(name, texture);
    }

    public Texture getTexture(String name) {
        Texture texture = textureMap.get(name);
        if (texture == null)
            throw new RuntimeException("No texture by name " + name);

        return texture;
    }
}
