package org.extremely.engine.rendering.loading;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.util.function.Predicate.not;

public class ObjLoader {

    public IndexedModel load(String fileName) {
        var vertices = new ArrayList<Vector3f>();
        var textures = new ArrayList<Vector2f>();
        var normals = new ArrayList<Vector3f>();
        var triangles = new ArrayList<List<VertexData>>();

        try (var lines = Files.lines(new File(fileName).toPath())) {
            lines
                    .filter(not(Objects::isNull))
                    .filter(line -> !line.startsWith("#"))
                    .map(line -> !line.contains("#") ? line : line.substring(0, line.indexOf("#")))
                    .map(String::trim)
                    .map( line -> line.split(" "))
                    .forEach(parts -> {
                        switch (parts[0]) {
                            case "v" -> vertices.add(new Vector3f(
                                    parseFloat(parts[1]),
                                    parseFloat(parts[2]),
                                    parseFloat(parts[3])));

                            case "vt" -> textures.add(new Vector2f(
                                    parseFloat(parts[1]),
                                    1.0f - parseFloat(parts[2])));

                            case "vn" -> normals.add(new Vector3f(
                                    parseFloat(parts[1]),
                                    parseFloat(parts[2]),
                                    parseFloat(parts[3])));

                            case "f" -> triangles.add(Arrays.stream(parts)
                                    .skip(1)
                                    .map(vertexData -> {
                                        var elements = vertexData.split("/");
                                        return new VertexData(parseInt(elements[0]) - 1, parseInt(elements[1]) - 1, parseInt(elements[2]) - 1);
                                    })
                                    .toList());
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException("Cannot load mesh " + fileName, e);
        }

        var indexArray = new ArrayList<Integer>();
        var vertexArray = new ArrayList<Float>();
        var textureArray = new ArrayList<Float>();
        var normalArray = new ArrayList<Float>();
        var vertexDataMap = new HashMap<VertexData, Integer>();
        triangles.forEach(vertexDataList -> {
            processVertexData(vertexDataList.get(0), vertexDataMap, indexArray, vertices, textures, normals, vertexArray, textureArray, normalArray);
            processVertexData(vertexDataList.get(1), vertexDataMap, indexArray, vertices, textures, normals, vertexArray, textureArray, normalArray);
            processVertexData(vertexDataList.get(2), vertexDataMap, indexArray, vertices, textures, normals, vertexArray, textureArray, normalArray);
        });

        return new IndexedModel(vertexArray, textureArray, normalArray, indexArray);
    }

    private void processVertexData(VertexData vertexData,
                                   Map<VertexData, Integer> vertexDataMap,
                                   List<Integer> indices,
                                   List<Vector3f> vertices,
                                   List<Vector2f> textures,
                                   List<Vector3f> normals,
                                   List<Float> vertexArray,
                                   List<Float> textureArray,
                                   List<Float> normalArray) {
        var index = vertexDataMap.get(vertexData);
        if (index == null) {
            index = vertexDataMap.size();
            vertexDataMap.put(vertexData, index);

            var currentVertex = vertices.get(vertexData.vertex);
            vertexArray.add(currentVertex.x);
            vertexArray.add(currentVertex.y);
            vertexArray.add(currentVertex.z);

            var currentTexture = textures.get(vertexData.texture);
            textureArray.add(currentTexture.x);
            textureArray.add(currentTexture.y);

            var currentNormal = normals.get(vertexData.normal);
            normalArray.add(currentNormal.x);
            normalArray.add(currentNormal.y);
            normalArray.add(currentNormal.z);
        }

        indices.add(index);
    }

    record VertexData(Integer vertex, Integer texture, Integer normal) {}
}
