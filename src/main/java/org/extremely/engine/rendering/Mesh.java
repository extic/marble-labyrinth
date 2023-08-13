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

import org.extremely.engine.core.Utils;
import org.extremely.engine.rendering.loading.IndexedModel;
import org.extremely.engine.rendering.loading.ObjLoader;
import org.extremely.engine.rendering.resources.MeshResource;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public class Mesh {
    private static final Map<String, MeshResource> loadedModels = new HashMap<>();

    private final MeshResource resource;
    private final String fileName;



    public Mesh(String fileName) {
        this.fileName = fileName;
        MeshResource oldResource = loadedModels.get(fileName);

        if (oldResource != null) {
            resource = oldResource;
//            resource.AddReference();
        } else {
            resource = loadMesh(fileName);
            loadedModels.put(fileName, resource);
        }
    }

//    @Override
//    protected void finalize() {
//        if (resource.RemoveReference() && !fileName.isEmpty()) {
//            loadedModels.remove(fileName);
//        }
//    }

    public void draw() {
        GL30.glBindVertexArray(resource.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
//        glEnableVertexAttribArray(2);
//        glEnableVertexAttribArray(3);
//
//        glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
//        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
//        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
//        glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
//        glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32);
//
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
//        glDrawElements(GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0);

        glDrawElements(GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
//        glDisableVertexAttribArray(2);
//        glDisableVertexAttribArray(3);
        GL30.glBindVertexArray(0);
    }

    private MeshResource loadMesh(String fileName) {
        if (!fileName.toLowerCase().endsWith(".obj")) {
            throw new RuntimeException("File is of unsupported mesh format - " + fileName);
        }

        ObjLoader loader = new ObjLoader();
        IndexedModel model = loader.load("./res/models/" + fileName);

        int vaoId = loadToVao(model);
        return new MeshResource(vaoId, model.indicesArray().length);
    }

    private int loadToVao(IndexedModel model) {
        var vaoId = createVao();
        bindIndicesBuffer(model.indicesArray());
        storeDataInAttributeList(0, 3, model.verticesArray());
        storeDataInAttributeList(1, 2, model.textureArray());
        storeDataInAttributeList(2, 3, model.normalArray());
        GL30.glBindVertexArray(0);
        return vaoId;
    }

    private int createVao() {
        var vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        return vaoId;
    }

    private void bindIndicesBuffer(int[] indices) {
        var vboId = GL15.glGenBuffers();
//        vbos.add(vboId)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        var buffer = Utils.createFlippedBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        var vboId = GL15.glGenBuffers();
//        vbos.add(vboId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        var buffer = Utils.createFlippedBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
}
