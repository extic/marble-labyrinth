package org.extremely.marble;

import org.extremely.engine.core.SceneComponent;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BoardMovement extends SceneComponent {
    private Vector3f input = new Vector3f(0, 0, 0);

    public void setInput(Vector3f input) {
        this.input = input;
    }

    @Override
    public void input() {
        var transform = getTransform();
        transform.setRot(new Quaternionf().rotateXYZ(input.x, input.y, input.z));
        transform.setModified(true);
    }
}
