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
import org.extremely.engine.core.components.MeshRenderer;
import org.extremely.engine.rendering.Material;
import org.extremely.engine.rendering.Mesh;
import org.extremely.engine.rendering.RenderingEngine;
import org.extremely.engine.rendering.Texture;

public class MarbleGame implements Game {

    @Override
    public EngineSettings getSettings() {
        return new EngineSettings(new Dimension(800, 600), "Labyrinth Marble", 60, true);
    }

    @Override
    public void init() {
        var sceneGraph = Engine.getInstance().getSceneGraph();

        Mesh mesh = new Mesh("plane3.obj");
        Material material = new Material(new Texture("bricks2.jpg"));

        MeshRenderer meshRenderer = new MeshRenderer(mesh, material);

        SceneObject plane = new SceneObject();
        plane.add(meshRenderer);
//        plane.GetTransform().GetPos().Set(0, -1, 5);

        sceneGraph.add(plane);
    }

    @Override
    public void update(float frameTime) {

    }

    @Override
    public void render(RenderingEngine renderingEngine) {

    }
}
