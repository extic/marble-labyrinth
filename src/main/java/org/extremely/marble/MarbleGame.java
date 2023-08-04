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

import org.extremely.engine.core.Dimension;
import org.extremely.engine.core.EngineSettings;
import org.extremely.engine.core.Game;
import org.extremely.engine.rendering.RenderingEngine;

public class MarbleGame implements Game {

    @Override
    public EngineSettings getSettings() {
        return new EngineSettings(new Dimension(800, 600), "Labyrinth Marble", 60, true);
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float frameTime) {

    }

    @Override
    public void render(RenderingEngine renderingEngine) {

    }
}
