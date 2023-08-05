package org.extremely.engine.rendering.resources;

import static org.lwjgl.opengl.GL11.glGenTextures;

public class TextureResource {
    private int id;
//    private int refCount;

    public TextureResource() {
        this.id = glGenTextures();
//        this.refCount = 1;
    }
//
//    @Override
//    protected void finalize() {
//        glDeleteBuffers(id);
//    }

//    public void AddReference() {
//        refCount++;
//    }
//
//    public boolean RemoveReference() {
//        refCount--;
//        return refCount == 0;
//    }

    public int getId() {
        return id;
    }
}
