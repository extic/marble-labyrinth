package org.extremely.engine.rendering;

import org.extremely.engine.core.EngineSettings;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Window {
    private long windowHandle;

    void init(EngineSettings settings) {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

        var maximized = false;
        var dimension = settings.dimension();
        if (dimension.getWidth() == 0 || dimension.getHeight() == 0) {
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            dimension.setWidth(100);
            dimension.setHeight(100);
            maximized = true;
        }

        windowHandle = GLFW.glfwCreateWindow(dimension.getWidth(), dimension.getHeight(), settings.windowTitle(), MemoryUtil.NULL, MemoryUtil.NULL);
        if (windowHandle == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        GLFW.glfwSetFramebufferSizeCallback(windowHandle, (windowHandle, width, height) -> {
            dimension.setWidth(width);
            dimension.setHeight(height);
        });

//        GLFW.glfwSetKeyCallback(windowHandle) { window, key, _, action, _ ->
//            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
//                GLFW.glfwSetWindowShouldClose(window, true)
//            }
//        }

        if (maximized) {
            GLFW.glfwMaximizeWindow(windowHandle);
        } else {
            var vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            if (vidMode == null) {
                throw new RuntimeException("Cannot get current video mode");
            }

            GLFW.glfwSetWindowPos(windowHandle, (vidMode.width() - dimension.getWidth()) / 2, (vidMode.height() - dimension.getHeight()) / 2);
        }

        GLFW.glfwMakeContextCurrent(windowHandle);

        if (settings.vSync()) {
            GLFW.glfwSwapInterval(1);
        }

        GLFW.glfwShowWindow(windowHandle);

        GL.createCapabilities();
//
//        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        GL11.glEnable(GL11.GL_DEPTH_TEST);
//        GL11.glEnable(GL11.GL_STENCIL_TEST);
//        GL11.glEnable(GL11.GL_CULL_FACE);
//        GL11.glCullFace(GL11.GL_BACK);

        GLFW.glfwSetWindowTitle(windowHandle, settings.windowTitle());
    }

    public boolean isCloseRequested() {
        return GLFW.glfwWindowShouldClose(windowHandle);
    }

    public void update() {
        GLFW.glfwSwapBuffers(windowHandle);
        GLFW.glfwPollEvents();
    }

    public void dispose() {
        GLFW.glfwDestroyWindow(windowHandle);
    }
}
