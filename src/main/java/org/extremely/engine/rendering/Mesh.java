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

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

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
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
//        glEnableVertexAttribArray(2);
//        glEnableVertexAttribArray(3);
//
        glBindBuffer(GL_ARRAY_BUFFER, resource.GetVbo());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
//        glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
//        glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32);
//
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.GetIbo());
        glDrawElements(GL_TRIANGLES, resource.GetSize(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
//        glDisableVertexAttribArray(2);
//        glDisableVertexAttribArray(3);
    }

    private MeshResource loadMesh(String fileName) {
        if (!fileName.toLowerCase().endsWith(".obj")) {
            throw new RuntimeException("File is of unsupported mesh format - " + fileName);
        }

        ObjLoader loader = new ObjLoader();
        IndexedModel model = loader.load("./res/models/" + fileName);

        Vertex[] vertexData = convertToVertices(model);
        int[] indexData = convertToIndexes(model);

        var resource = new MeshResource(indexData.length);

        glBindBuffer(GL_ARRAY_BUFFER, resource.GetVbo());
        glBufferData(GL_ARRAY_BUFFER, Utils.createFlippedBuffer(vertexData), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.GetIbo());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utils.createFlippedBuffer(indexData), GL_STATIC_DRAW);

        return resource;
    }

    private Vertex[] convertToVertices(IndexedModel model) {
        Vertex[] vertexData = new Vertex[model.getPositions().size()];
        for(int i = 0; i < model.getPositions().size(); i++)
        {
            vertexData[i] = new Vertex(
                    model.getPositions().get(i),
                    model.getTexCoords().get(i),
                    model.getNormals().get(i));
        }


        return vertexData;
    }

    private int[] convertToIndexes(IndexedModel model) {
        return model.getIndices().stream().mapToInt(i -> i).toArray();
    }
}
