package org.extremely.marble;

import org.extremely.engine.core.SceneComponent;
import org.extremely.engine.core.SceneObject;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class BallMovement extends SceneComponent {

    private final SceneObject board;
    private final WallCollisionDetector wallCollisionDetector;
    private final HoleCollisionDetector holeCollisionDetector;
    private Vector3f acceleration;
    private Vector3f velocity;
    private Vector3f spin;
    private boolean falling;
    private boolean moving;
    private boolean buttonPressed;

    public BallMovement(SceneObject board) {
        this.board = board;

        wallCollisionDetector = new WallCollisionDetector();
        holeCollisionDetector = new HoleCollisionDetector();
        buttonPressed = false;

        reset();


        velocity = new Vector3f(0, 0, 0.005f);
    }

    private void reset() {
        this.acceleration = new Vector3f(0, 0, 0);
        this.velocity = new Vector3f(0.01f, 0, 0.005f);
        this.spin = null;

        falling = false;
        moving = true;
    }

    public void setInput(boolean button) {
        this.buttonPressed = button;
    }

    @Override
    public void input() {
        if (buttonPressed) {
            reset();

            getTransform().reset();
            getTransform().setPos(new Vector3f(1.36f, 0.25f, -4.56f));
            getTransform().setModified(true);
        }
    }

    @Override
    public void update(float frameTime) {
        if (!moving) {
            return;
        }

        if (falling) {
            handleFalling();
            return;
        }

        updateAcceleration();

        velocity.add(acceleration);

        detectWallCollision();
        rotateBall();
        detectFallingToHole();

        getTransform().setModified(true);
    }

    private void updateAcceleration() {
        var boardMatrix = board.getTransform().getTransformMatrix();
        var boardNormal = new Vector4f(0, 1, 0, 0).mul(boardMatrix);
        var upVector = new Vector3f(0, 1, 0);
        var boardTangent = upVector.cross(boardNormal.x, boardNormal.y, boardNormal.z);
        if (boardTangent.length() > 0.001) {
            var gradient = boardTangent.cross(boardNormal.x, boardNormal.y, boardNormal.z).normalize();

            acceleration.zero().add(gradient.mul(-gradient.y).mul(0.02f));
            acceleration.y = 0;
        }
    }

    private void rotateBall() {
        var distance = velocity.length();
        if (distance == 0) {
            return;
        }
        var rotationAngle = distance / 0.25f;

        var newSpin = new Vector3f();
        var normalizedVelocity = new Vector3f();
        velocity.normalize(normalizedVelocity);
        new Vector3f(0, 1, 0).cross(normalizedVelocity, newSpin);
        newSpin.normalize();

        if (spin != null) {
            var orig = getTransform().getRot();
            var quaternion = new Quaternionf().identity().rotateAxis(rotationAngle, newSpin);
            quaternion.mul(orig);
            getTransform().setRot(quaternion);
        }
        spin = newSpin;
    }

    private void detectWallCollision() {
        var beforeTranslation = getTransform().getPos();
        var afterTranslation = new Vector3f();
        beforeTranslation.add(velocity, afterTranslation);
        wallCollisionDetector.detect(beforeTranslation, afterTranslation, velocity, 0.25f);
        getTransform().setPos(afterTranslation);
    }

    private void detectFallingToHole() {
        var pos = getTransform().getPos();
        Vector3f hole = holeCollisionDetector.detect(pos, 0.3f);
        if (hole != null) {
            falling = true;
            hole.sub(pos, velocity);
            velocity.y = 0;
            velocity.div(10f);
        }
    }

    private void handleFalling() {
        velocity.add(new Vector3f(0f, -0.01f, 0f));
        getTransform().getPos().add(velocity);
        getTransform().setModified(true);

        if (getTransform().getPos().y < -1f) {
            moving = false;
        }
    }
}
