package org.extremely.engine.core;

public record EngineSettings(
    int width,
    int height,
    String windowTitle,
    int frameRate,
    boolean vSync
) {
    public float getFrameTime() {
        return 1.0f / (float)frameRate;
    }
}

