package org.extremely.engine.core;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SceneObjectTransform {
    private Vector3f pos;
    private Quaternionf rot;
    private Vector3f scale;
    private Matrix4f transform;
    private boolean modified;

    public SceneObjectTransform() {
        pos = new Vector3f(0, 0, 0);
        rot = new Quaternionf(0, 0, 0, 1);
        scale = new Vector3f(1, 1, 1);
        transform = new Matrix4f().identity();
        modified = false;
    }

    public void update(Matrix4f parentTransformation) {
        Matrix4f translationMatrix = new Matrix4f().translation(pos);
        Matrix4f rotationMatrix = new Matrix4f().set(rot);
        Matrix4f scaleMatrix = new Matrix4f().scaling(scale);

        parentTransformation.mul(translationMatrix.mul(rotationMatrix.mul(scaleMatrix)), transform);
    }

    public Matrix4f getTransformMatrix() {
        return transform;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Quaternionf getRot() {
        return rot;
    }

    public void setRot(Quaternionf rot) {
        this.rot = rot;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }
}
