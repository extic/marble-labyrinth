package org.extremely.marble;

import org.extremely.engine.core.SceneComponent;
import org.extremely.engine.core.SceneObject;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class BallMovement extends SceneComponent {

    private final SceneObject board;
    private Vector3f acceleration;
    private Vector3f velocity;

    public BallMovement(SceneObject board) {
        this.board = board;
        this.acceleration = new Vector3f(0, 0, 0);
        this.velocity = new Vector3f(0, 0, 0);
    }

    @Override
    public void update(float frameTime) {
        var boardMatrix = board.getTransform().getLocalMatrix();
        var boardNormal = new Vector4f(0, 1, 0, 0).mul(boardMatrix);
        var upVector = new Vector3f(0, 1, 0);
        var boardTangent = upVector.cross(boardNormal.x, boardNormal.y, boardNormal.z);
        if (boardTangent.length() > 0.001) {
            var gradient = boardTangent.cross(boardNormal.x, boardNormal.y, boardNormal.z).normalize();

            acceleration.zero().add(gradient.mul(-gradient.y).mul(0.01f));
            acceleration.y = 0;

            velocity.add(acceleration);

            getTransform().getLocalMatrix().translate(velocity);
            getTransform().setChanged(true);
        }
    }
}