package org.extremely.engine.rendering;

import org.extremely.engine.core.EngineSettings;

public class RenderingEngine {
    private Window window;

    public void init(EngineSettings settings) {
        window = new Window();
        window.init(settings);
    }

    public void render() {
    }

    public boolean isCloseRequested() {
        return window.isCloseRequested();
    }

    public void dispose() {
        window.dispose();
    }
}
