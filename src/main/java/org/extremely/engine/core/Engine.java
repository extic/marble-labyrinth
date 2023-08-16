package org.extremely.engine.core;

import org.extremely.engine.rendering.RenderingEngine;

public class Engine {
    private static final Engine instance = new Engine();

    private boolean running;
    private Game game;
    private RenderingEngine renderingEngine;
    private SceneGraph sceneGraph;


    public static Engine getInstance() {
        return instance;
    }

    public void init(Game game) {
        this.game = game;

        running = false;
        renderingEngine = new RenderingEngine();
        renderingEngine.init(game.getSettings());
        sceneGraph = new SceneGraph();
    }

    public void start() {
        if (running)
            return;

        run();
    }

    public void stop() {
        running = false;
    }

    private void run() {
        running = true;

        int frames = 0;
        double frameCounter = 0;
        var frameTime = game.getSettings().getFrameTime();

        game.init();

        double lastTime = Time.getTime();
        double unprocessedTime = 0;

        while (running) {
            boolean render = false;

            double startTime = Time.getTime();
            double passedTime = startTime - lastTime;
//            System.out.println(passedTime);
            lastTime = startTime;

            unprocessedTime += passedTime;
            frameCounter += passedTime;

            game.input();

            while (unprocessedTime > frameTime) {
                render = true;

                unprocessedTime -= frameTime;

//                game.input(frameTime);
//                input.update();


                game.update(frameTime);

                if (frameCounter >= 1.0) {
                    System.out.println(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (render) {
                renderingEngine.render(sceneGraph);
                frames++;
            } else {
                Utils.delay(1);
            }

            if (renderingEngine.isCloseRequested()) {
                stop();
            }

        }

        cleanUp();
    }

    private void cleanUp() {
        renderingEngine.dispose();
    }

    public RenderingEngine getRenderingEngine() {
        return renderingEngine;
    }

    public SceneGraph getSceneGraph() {
        return sceneGraph;
    }
}
