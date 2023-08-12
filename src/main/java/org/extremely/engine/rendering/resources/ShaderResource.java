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

package org.extremely.engine.rendering.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.glCreateProgram;

public class ShaderResource {
    private final int program;
    private final Map<String, Integer> uniforms;
    private final List<String> uniformNames;
    private final List<String> uniformTypes;
//    private int refCount;

    public ShaderResource() {
        this.program = glCreateProgram();
//        this.refCount = 1;

        if (program == 0) {
            throw new RuntimeException("Shader creation failed: Could not find valid memory location in constructor");
        }

        uniforms = new HashMap<>();
        uniformNames = new ArrayList<>();
        uniformTypes = new ArrayList<>();
    }
//
//    @Override
//    protected void finalize() {
//        glDeleteBuffers(program);
//    }
//
//    public void AddReference() {
//        refCount++;
//    }
//
//    public boolean RemoveReference() {
//        refCount--;
//        return refCount == 0;
//    }

    public int getProgram() {
        return program;
    }

    public Map<String, Integer> getUniforms() {
        return uniforms;
    }

    public List<String> getUniformNames() {
        return uniformNames;
    }

    public List<String> getUniformTypes() {
        return uniformTypes;
    }

    public void addUniform(String uniformName, int uniformLocation) {
        uniforms.put(uniformName, uniformLocation);
    }
}
