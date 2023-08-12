package org.extremely.engine.core.components;

import org.extremely.engine.core.Engine;
import org.extremely.engine.core.SceneComponent;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera extends SceneComponent
{
	private Matrix4f projectionMatrix;
	private Vector3f position;
	private Vector3f center;
	private Vector3f up;

	public Camera(float fov, float zNear, float zFar, Vector3f position, Vector3f center, Vector3f up) {
		this.position = position;
		this.center = center;
		this.up = up;
		
		var window = Engine.getInstance().getRenderingEngine().getWindow();
		var aspectRatio = (float) window.getWidth() / (float)window.getHeight();
		projectionMatrix = new Matrix4f().perspective(Math.toRadians(fov), aspectRatio, zNear, zFar);
	}

	public Matrix4f getViewMatrix() {
		return new Matrix4f().setLookAt(position, center, up);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
