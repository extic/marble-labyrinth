package org.extremely.engine.core;

public record EngineSettings(
    Dimension dimension,
    String windowTitle,
    int frameRate,
    boolean vSync
) {
    public float getFrameTime() {
        return 1.0f / (float)frameRate;
    }
}

