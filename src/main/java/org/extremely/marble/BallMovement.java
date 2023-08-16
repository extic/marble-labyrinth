package org.extremely.marble;

import org.extremely.engine.core.SceneComponent;

public class BallMovement extends SceneComponent {
    float counter = 0.0f;
    float acceleration = 0.02f;
    float velocity = 0.0f;

    @Override
    public void update(float frameTime) {
        counter += frameTime;
        acceleration = (float)Math.cos(counter) / 20;
//        System.out.println(acceleration);
        velocity += acceleration * frameTime;

        getTransform().getLocalMatrix().translate(velocity, 0, 0);
        getTransform().setChanged(true);
    }
}
