package org.extremely.marble;

import org.extremely.engine.core.SceneComponent;
import org.extremely.engine.core.SceneObject;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class BallMovement extends SceneComponent {

    private final SceneObject board;
    private final WallCollisionDetector wallCollisionDetector;
    private Vector3f acceleration;
    private Vector3f velocity;

    public BallMovement(SceneObject board) {
        this.board = board;
        this.acceleration = new Vector3f(0, 0, 0);
        this.velocity = new Vector3f(0.01f, 0, 0.005f);

        wallCollisionDetector = new WallCollisionDetector();
    }

    float test = 0f;
    @Override
    public void update(float frameTime) {
        var boardMatrix = board.getTransform().getTransformMatrix();
        var boardNormal = new Vector4f(0, 1, 0, 0).mul(boardMatrix);
        var upVector = new Vector3f(0, 1, 0);
        var boardTangent = upVector.cross(boardNormal.x, boardNormal.y, boardNormal.z);
        if (boardTangent.length() > 0.001) {
            var gradient = boardTangent.cross(boardNormal.x, boardNormal.y, boardNormal.z).normalize();

            acceleration.zero().add(gradient.mul(-gradient.y).mul(0.02f));
            acceleration.y = 0;
        }

        velocity.add(acceleration);

        var beforeTranslation = getTransform().getPos();
        var afterTranslation = new Vector3f();
        beforeTranslation.add(velocity, afterTranslation);
        wallCollisionDetector.detect(beforeTranslation, afterTranslation, velocity, 0.25f);
        getTransform().setPos(afterTranslation);

        var rotationVector = new Vector3f();

        var temp = new Vector3f();
        afterTranslation.sub(beforeTranslation, temp);
        var length = temp.length();
        if (length != 0f) {
            temp.cross(new Vector3f(0, -1f, 0), rotationVector);
            getTransform().setRot(new Quaternionf().rotateAxis(test, rotationVector));
//        var distance = beforeTranslation.distance(afterTranslation);
            test += length * 5f;
//        if (test > 2f * Math.PI) {
//            test -= (float) (2f * Math.PI);
//        }
        }



        getTransform().setModified(true);
    }
}
