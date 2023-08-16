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

public class MarbleGame implements Game {
    private InputServer inputServer;
    private float counter;

    @Override
    public EngineSettings getSettings() {
        return new EngineSettings(1600, 900, "Labyrinth Marble", 60, true);
    }

    @Override
    public void init() {
        var sceneGraph = Engine.getInstance().getSceneGraph();

        var board = createBoard();
        sceneGraph.add(board);

        var ball = createBall(board);
        board.add(ball);

        var light = new Light(new Vector3f(0, 20, 20), new Vector3f(1, 1, 1));
        sceneGraph.setLight(light);
        sceneGraph.getRoot().add(createCamera());

        var boardInputComponent = new BoardMovement();
        board.add(boardInputComponent);
        inputServer = new InputServer();
        inputServer.run(vector -> {
            boardInputComponent.setInput(new Vector3f(Math.toRadians(-vector.y), 0, Math.toRadians(-vector.x)));
        });
    }

    @Override
    public void input() {
        Engine.getInstance().getSceneGraph().getRoot().input();
    }

    @Override
    public void update(float frameTime) {
        Engine.getInstance().getSceneGraph().getRoot().update(frameTime);
//        counter += 0.2f;
//        var rotate = new Matrix4f().rotate(0.001f, new Vector3f(0, 1, 0));
//        var rotate1 = new Matrix4f().rotate((float)(Math.sin(counter)) / 20f, new Vector3f(0, 0, 1));
//        var rotate2 = new Matrix4f().rotate((float)(Math.sin(counter / 1.5f)) / 20f, new Vector3f(1, 0, 0));
//        board.getTransform().mul(rotate).mul(rotate1).mul(rotate2);
    }

    @Override
    public void render(RenderingEngine renderingEngine) {

    }

    private Camera createCamera() {
        Vector3f position = new Vector3f(0f, 5f, 8f);
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
        material = new Material(new Texture("wood1.png"));
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
        var material = new Material(new Texture("wood1.png"));
        var meshRenderer = new MeshRenderer(mesh, material);
        SceneObject ball = new SceneObject("ball");
        ball.add(meshRenderer);
        ball.getTransform().getLocalMatrix().translate(new Vector3f(0, 0.125f, 0));
        ball.getTransform().setChanged(true);
        ball.add(new BallMovement(board));
        return ball;
    }
}
