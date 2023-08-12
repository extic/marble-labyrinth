package org.extremely.engine.core.components;

import org.extremely.engine.core.SceneComponent;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera extends SceneComponent
{
	private Matrix4f projectionMatrix;
	private Vector3f position;
	private Vector3f center;
	private Vector3f up;

	public Camera(Matrix4f projection) {
		this.projectionMatrix = projection;

		position = new Vector3f(0f, 2f, 3f);
		center = new Vector3f(0f, 0f, 0f);
		up = new Vector3f(0f, 1f, 0f);
	}

	public Matrix4f getViewMatrix() {
		return new Matrix4f().setLookAt(position, center, up);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
