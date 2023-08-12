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

package org.extremely.engine.rendering;

import org.extremely.engine.core.Engine;
import org.extremely.engine.rendering.resources.ShaderResource;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class Shader {
    private final static Map<String, ShaderResource> loadedShaders = new HashMap<>();

    private ShaderResource resource;
    private String fileName;

    public Shader(String fileName) {
        this.fileName = fileName;

        ShaderResource oldResource = loadedShaders.get(fileName);

        if (oldResource != null) {
            resource = oldResource;
//            resource.AddReference();
        } else {
            resource = new ShaderResource();

            var vertexShaderText = loadShader(fileName + ".vert");
            var fragmentShaderText = loadShader(fileName + ".frag");

            addVertexShader(vertexShaderText);
            addFragmentShader(fragmentShaderText);

            addAllAttributes(vertexShaderText);

            compileShader();
//
//            addAllUniforms(vertexShaderText);
//            addAllUniforms(fragmentShaderText);



            loadedShaders.put(fileName, resource);

            addUniform("textureSampler", "sampler2D");
            addUniform("transformationMatrix", "mat4");
            addUniform("viewMatrix", "mat4");
            addUniform("projectionMatrix", "mat4");
        }
    }
//
//    @Override
//    protected void finalize() {
//        if (resource.RemoveReference() && !fileName.isEmpty()) {
//            loadedShaders.remove(fileName);
//        }
//    }

    public void bind() {
        glUseProgram(resource.getProgram());
    }

//    public void UpdateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
    public void updateUniforms(Matrix4f transform, Material material, RenderingEngine renderingEngine) {
        int samplerSlot = renderingEngine.getSamplerSlot("diffuse");
        material.getTexture("diffuse").bind(samplerSlot);
        setUniform("textureSampler", samplerSlot);

        var identity = new Matrix4f().identity();
        var camera = Engine.getInstance().getSceneGraph().getCamera();
        setUniform("transformationMatrix", identity);
        setUniform("viewMatrix", camera.getViewMatrix());
        setUniform("projectionMatrix", camera.getProjectionMatrix());


//        Matrix4f worldMatrix = transform.getTransformation();
//        Matrix4f MVPMatrix = renderingEngine.getMainCamera().GetViewProjection().Mul(worldMatrix);

//        for (int i = 0; i < resource.getUniformNames().size(); i++) {
//            String uniformName = resource.getUniformNames().get(i);
//            String uniformType = resource.getUniformTypes().get(i);
//
//            if (uniformType.equals("sampler2D")) {
//                int samplerSlot = renderingEngine.getSamplerSlot(uniformName);
//                material.getTexture(uniformName).bind(samplerSlot);
//                SetUniformi(uniformName, samplerSlot);
//            } else if (uniformName.startsWith("T_")) {
//                if (uniformName.equals("T_MVP"))
//                    SetUniform(uniformName, MVPMatrix);
//                else if (uniformName.equals("T_model"))
//                    SetUniform(uniformName, worldMatrix);
//                else
//                    throw new IllegalArgumentException(uniformName + " is not a valid component of Transform");
//            } else if (uniformName.startsWith("R_")) {
//                String unprefixedUniformName = uniformName.substring(2);
//                if (uniformType.equals("vec3"))
//                    SetUniform(uniformName, renderingEngine.GetVector3f(unprefixedUniformName));
//                else if (uniformType.equals("float"))
//                    SetUniformf(uniformName, renderingEngine.GetFloat(unprefixedUniformName));
//                else if (uniformType.equals("DirectionalLight"))
//                    SetUniformDirectionalLight(uniformName, (DirectionalLight) renderingEngine.GetActiveLight());
//                else if (uniformType.equals("PointLight"))
//                    SetUniformPointLight(uniformName, (PointLight) renderingEngine.GetActiveLight());
//                else if (uniformType.equals("SpotLight"))
//                    SetUniformSpotLight(uniformName, (SpotLight) renderingEngine.GetActiveLight());
//                else
//                    renderingEngine.UpdateUniformStruct(transform, material, this, uniformName, uniformType);
//            } else if (uniformName.startsWith("C_")) {
//                if (uniformName.equals("C_eyePos"))
//                    SetUniform(uniformName, renderingEngine.GetMainCamera().GetTransform().GetTransformedPos());
//                else
//                    throw new IllegalArgumentException(uniformName + " is not a valid component of Camera");
//            } else {
//                if (uniformType.equals("vec3"))
//                    SetUniform(uniformName, material.GetVector3f(uniformName));
//                else if (uniformType.equals("float"))
//                    SetUniformf(uniformName, material.GetFloat(uniformName));
//                else
//                    throw new IllegalArgumentException(uniformType + " is not a supported type in Material");
//            }
//        }
    }

    private void addAllAttributes(String shaderText) {
        setAttribLocation(0, "position");
        setAttribLocation(1, "textureCoords");


//        final String ATTRIBUTE_KEYWORD = "attribute";
//        int attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD);
//        int attribNumber = 0;
//        while (attributeStartLocation != -1) {
//            if (!(attributeStartLocation != 0
//                    && (Character.isWhitespace(shaderText.charAt(attributeStartLocation - 1)) || shaderText.charAt(attributeStartLocation - 1) == ';')
//                    && Character.isWhitespace(shaderText.charAt(attributeStartLocation + ATTRIBUTE_KEYWORD.length())))) {
//                attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length());
//                continue;
//
//            }
//
//            int begin = attributeStartLocation + ATTRIBUTE_KEYWORD.length() + 1;
//            int end = shaderText.indexOf(";", begin);
//
//            String attributeLine = shaderText.substring(begin, end).trim();
//            String attributeName = attributeLine.substring(attributeLine.indexOf(' ') + 1, attributeLine.length()).trim();
//
//            SetAttribLocation(attributeName, attribNumber);
//            attribNumber++;
//
//            attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length());
//        }
    }
//
//    private class GLSLStruct {
//        public String name;
//        public String type;
//    }
//
//    private HashMap<String, ArrayList<GLSLStruct>> findUniformStructs(String shaderText) {
//        HashMap<String, ArrayList<GLSLStruct>> result = new HashMap<String, ArrayList<GLSLStruct>>();
//
//        final String STRUCT_KEYWORD = "struct";
//        int structStartLocation = shaderText.indexOf(STRUCT_KEYWORD);
//        while (structStartLocation != -1) {
//            if (!(structStartLocation != 0
//                    && (Character.isWhitespace(shaderText.charAt(structStartLocation - 1)) || shaderText.charAt(structStartLocation - 1) == ';')
//                    && Character.isWhitespace(shaderText.charAt(structStartLocation + STRUCT_KEYWORD.length())))) {
//                structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length());
//                continue;
//            }
//
//            int nameBegin = structStartLocation + STRUCT_KEYWORD.length() + 1;
//            int braceBegin = shaderText.indexOf("{", nameBegin);
//            int braceEnd = shaderText.indexOf("}", braceBegin);
//
//            String structName = shaderText.substring(nameBegin, braceBegin).trim();
//            ArrayList<GLSLStruct> glslStructs = new ArrayList<GLSLStruct>();
//
//            int componentSemicolonPos = shaderText.indexOf(";", braceBegin);
//            while (componentSemicolonPos != -1 && componentSemicolonPos < braceEnd) {
//                int componentNameEnd = componentSemicolonPos + 1;
//
//                while (Character.isWhitespace(shaderText.charAt(componentNameEnd - 1)) || shaderText.charAt(componentNameEnd - 1) == ';')
//                    componentNameEnd--;
//
//                int componentNameStart = componentSemicolonPos;
//
//                while (!Character.isWhitespace(shaderText.charAt(componentNameStart - 1)))
//                    componentNameStart--;
//
//                int componentTypeEnd = componentNameStart;
//
//                while (Character.isWhitespace(shaderText.charAt(componentTypeEnd - 1)))
//                    componentTypeEnd--;
//
//                int componentTypeStart = componentTypeEnd;
//
//                while (!Character.isWhitespace(shaderText.charAt(componentTypeStart - 1)))
//                    componentTypeStart--;
//
//                String componentName = shaderText.substring(componentNameStart, componentNameEnd);
//                String componentType = shaderText.substring(componentTypeStart, componentTypeEnd);
//
//                GLSLStruct glslStruct = new GLSLStruct();
//                glslStruct.name = componentName;
//                glslStruct.type = componentType;
//
//                glslStructs.add(glslStruct);
//
//                componentSemicolonPos = shaderText.indexOf(";", componentSemicolonPos + 1);
//            }
//
//            result.put(structName, glslStructs);
//
//            structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length());
//        }
//
//        return result;
//    }
//
//    private void addAllUniforms(String shaderText) {
//        HashMap<String, ArrayList<GLSLStruct>> structs = FindUniformStructs(shaderText);
//
//        final String UNIFORM_KEYWORD = "uniform";
//        int uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD);
//        while (uniformStartLocation != -1) {
//            if (!(uniformStartLocation != 0
//                    && (Character.isWhitespace(shaderText.charAt(uniformStartLocation - 1)) || shaderText.charAt(uniformStartLocation - 1) == ';')
//                    && Character.isWhitespace(shaderText.charAt(uniformStartLocation + UNIFORM_KEYWORD.length())))) {
//                uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length());
//                continue;
//            }
//
//            int begin = uniformStartLocation + UNIFORM_KEYWORD.length() + 1;
//            int end = shaderText.indexOf(";", begin);
//
//            String uniformLine = shaderText.substring(begin, end).trim();
//
//            int whiteSpacePos = uniformLine.indexOf(' ');
//            String uniformName = uniformLine.substring(whiteSpacePos + 1, uniformLine.length()).trim();
//            String uniformType = uniformLine.substring(0, whiteSpacePos).trim();
//
//            resource.GetUniformNames().add(uniformName);
//            resource.GetUniformTypes().add(uniformType);
//            AddUniform(uniformName, uniformType, structs);
//
//            uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length());
//        }
//    }
//
    private void addUniform(String uniformName, String uniformType) {
//    private void AddUniform(String uniformName, String uniformType, HashMap<String, ArrayList<GLSLStruct>> structs) {
//        boolean addThis = true;
//        ArrayList<GLSLStruct> structComponents = structs.get(uniformType);
//
//        if (structComponents != null) {
//            addThis = false;
//            for (GLSLStruct struct : structComponents) {
//                AddUniform(uniformName + "." + struct.name, struct.type, structs);
//            }
//        }
//
//        if (!addThis)
//            return;
//
        int uniformLocation = glGetUniformLocation(resource.getProgram(), uniformName);
        if (uniformLocation == 0xFFFFFFFF) {
            throw new RuntimeException("Error: Could not find uniform: " + uniformName);
        }

        resource.addUniform(uniformName, uniformLocation);
    }

    private void addVertexShader(String text) {
        addProgram(text, GL_VERTEX_SHADER);
    }

    private void AddGeometryShader(String text) {
        addProgram(text, GL_GEOMETRY_SHADER);
    }

    private void addFragmentShader(String text) {
        addProgram(text, GL_FRAGMENT_SHADER);
    }

    private void setAttribLocation(int attributeIndex, String attributeName) {
        glBindAttribLocation(resource.getProgram(), attributeIndex, attributeName);
    }

    private void compileShader() {
        glLinkProgram(resource.getProgram());

        if (glGetProgrami(resource.getProgram(), GL_LINK_STATUS) == 0) {
            throw new RuntimeException(glGetProgramInfoLog(resource.getProgram(), 1024));
        }

        glValidateProgram(resource.getProgram());

        if (glGetProgrami(resource.getProgram(), GL_VALIDATE_STATUS) == 0) {
            throw new RuntimeException(glGetProgramInfoLog(resource.getProgram(), 1024));
        }
    }

    private void addProgram(String text, int type) {
        int shader = glCreateShader(type);
        if (shader == 0) {
            throw new RuntimeException("Shader creation failed: Could not find valid memory location when adding shader");
        }

        glShaderSource(shader, text);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException(glGetShaderInfoLog(shader, 1024));
        }

        glAttachShader(resource.getProgram(), shader);
    }

    private static String loadShader(String fileName) {
        StringBuilder shaderSource = new StringBuilder();
        BufferedReader shaderReader = null;
        final String INCLUDE_DIRECTIVE = "#include";

        try {
            shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName));
            String line;

            while ((line = shaderReader.readLine()) != null) {
                if (line.startsWith(INCLUDE_DIRECTIVE)) {
                    shaderSource.append(loadShader(line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() - 1)));
                } else
                    shaderSource.append(line).append("\n");
            }

            shaderReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }


        return shaderSource.toString();
    }

    public void setUniform(String uniformName, int value) {
        glUniform1i(resource.getUniforms().get(uniformName), value);
    }

    public void setUniform(String uniformName, float value) {
        glUniform1f(resource.getUniforms().get(uniformName), value);
    }

    public void setUniform(String uniformName, Vector3f value) {
        glUniform3f(resource.getUniforms().get(uniformName), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (var it = MemoryStack.stackPush()) {
            glUniformMatrix4fv(resource.getUniforms().get(uniformName), false, value.get(it.mallocFloat(16)));
        }
    }
//
//    public void SetUniformBaseLight(String uniformName, BaseLight baseLight) {
//        SetUniform(uniformName + ".color", baseLight.GetColor());
//        SetUniformf(uniformName + ".intensity", baseLight.GetIntensity());
//    }
//
//    public void SetUniformDirectionalLight(String uniformName, DirectionalLight directionalLight) {
//        SetUniformBaseLight(uniformName + ".base", directionalLight);
//        SetUniform(uniformName + ".direction", directionalLight.GetDirection());
//    }
//
//    public void SetUniformPointLight(String uniformName, PointLight pointLight) {
//        SetUniformBaseLight(uniformName + ".base", pointLight);
//        SetUniformf(uniformName + ".atten.constant", pointLight.GetAttenuation().GetConstant());
//        SetUniformf(uniformName + ".atten.linear", pointLight.GetAttenuation().GetLinear());
//        SetUniformf(uniformName + ".atten.exponent", pointLight.GetAttenuation().GetExponent());
//        SetUniform(uniformName + ".position", pointLight.GetTransform().GetTransformedPos());
//        SetUniformf(uniformName + ".range", pointLight.GetRange());
//    }
//
//    public void SetUniformSpotLight(String uniformName, SpotLight spotLight) {
//        SetUniformPointLight(uniformName + ".pointLight", spotLight);
//        SetUniform(uniformName + ".direction", spotLight.GetDirection());
//        SetUniformf(uniformName + ".cutoff", spotLight.GetCutoff());
//    }
}
