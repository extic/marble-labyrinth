package org.extremely.engine.rendering.resources;

public class MeshResource {
    private int vaoId;
//    private int vbo;
//    private int ibo;
    private int size;
//    private int refCount;

    public MeshResource(int vaoId, int size) {
        this.vaoId = vaoId;

//        vbo = glGenBuffers();
//        ibo = glGenBuffers();

        this.size = size;
//        this.refCount = 1;
    }

    public void cleanup() {
//        glDeleteBuffers(vbo);
//        glDeleteBuffers(ibo);
    }
//
//    public void AddReference() {
//        refCount++;
//    }
//
//    public boolean RemoveReference() {
//        refCount--;
//        return refCount == 0;
//    }


    public int getVaoId() {
        return vaoId;
    }
//
//    public int getVbo() {
//        return vbo;
//    }
//
//    public int getIbo() {
//        return ibo;
//    }

    public int getSize() {
        return size;
    }
}
