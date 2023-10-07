/*
 * Copyright (C) 2014 Benny Bobaganoosh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.extremely.marble;

import org.extremely.engine.core.*;
import org.extremely.engine.core.components.Camera;
import org.extremely.engine.core.components.MeshRenderer;
import org.extremely.engine.rendering.Material;
import org.extremely.engine.rendering.Mesh;
import org.extremely.engine.rendering.RenderingEngine;
import org.extremely.engine.rendering.Texture;
import org.joml.Math;
import org.joml.Vector3f;

import java.io.File;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;

public class MarbleGame implements Game {
    private InputServer inputServer;

    @Override
    public EngineSettings getSettings() {
        return new EngineSettings(1600, 900, "Labyrinth Marble", 60, true);
    }

    @Override
    public void init() {
        var sceneGraph = Engine.getInstance().getSceneGraph();

        var board = createBoard();
        sceneGraph.add(board);

        loadWalls(board);

        var ball = createBall(board);
        board.add(ball);

        var light = new Light(new Vector3f(10, 20, 20), new Vector3f(1, 1, 1));
        sceneGraph.setLight(light);
        sceneGraph.getRoot().add(createCamera());

        var boardInputComponent = new BoardMovement();
        board.add(boardInputComponent);

        var ballInputComponent = new BallMovement(board);
        ball.add(ballInputComponent);

        inputServer = new InputServer();
        inputServer.run(input -> {
            boardInputComponent.setInput(new Vector3f(Math.toRadians(-input.vector().y), 0, Math.toRadians(-input.vector().x)));
            ballInputComponent.setInput(input.button1Pressed());
        });
    }

    @Override
    public void input() {
        Engine.getInstance().getSceneGraph().getRoot().input();
    }

    @Override
    public void update(float frameTime) {
        Engine.getInstance().getSceneGraph().getRoot().update(frameTime);
    }

    @Override
    public void render(RenderingEngine renderingEngine) {

    }

    private Camera createCamera() {
        Vector3f position = new Vector3f(0f, 10f, 4f);
        Vector3f center = new Vector3f(0f, 0f, 0f);
        Vector3f up = new Vector3f(0f, 1f, 0f);

        return new Camera(70.0f, 0.01f, 1000f, position, center, up);
    }

    private SceneObject createBoard() {
        Mesh mesh = new Mesh("board.obj");
        Material material = new Material(new Texture("board.png"));
        MeshRenderer meshRenderer = new MeshRenderer(mesh, material);
        SceneObject boardPlane = new SceneObject("board plane");
        boardPlane.add(meshRenderer);

        mesh = new Mesh("board-perimeter.obj");
        material = new Material(new Texture("wood2.jpg"));
        meshRenderer = new MeshRenderer(mesh, material);
        SceneObject boardPerimeter = new SceneObject("board perimeter");
        boardPerimeter.add(meshRenderer);

        var board = new SceneObject("board");
        board.add(boardPlane);
        board.add(boardPerimeter);
        return board;
    }

    private SceneObject createBall(SceneObject board) {
        var mesh = new Mesh("ball.obj");
        var material = new Material(new Texture("silver.jpg"));
        var meshRenderer = new MeshRenderer(mesh, material);
        SceneObject ball = new SceneObject("ball");
        ball.add(meshRenderer);
        ball.getTransform().setPos(new Vector3f(1.36f, 0.25f, -4.56f));
        ball.getTransform().setModified(true);
        return ball;
    }

    private void loadWalls(SceneObject board) {
        var modelsFolder = new File("./res/models");
        Arrays.stream(requireNonNull(modelsFolder.list((dir, name) -> name.matches("wall\\d+[.]obj"))))
                .forEach(fileName -> {
                    var mesh = new Mesh(fileName);
                    var material = new Material(new Texture("wood2.jpg"));
                    var meshRenderer = new MeshRenderer(mesh, material);
                    SceneObject wall = new SceneObject(fileName);
                    wall.add(meshRenderer);
                    board.add(wall);
                });
    }
}
