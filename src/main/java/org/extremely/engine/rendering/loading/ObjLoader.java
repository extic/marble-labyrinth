package org.extremely.engine.rendering.loading;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.util.function.Predicate.not;

public class ObjLoader {
//    private final List<Vector3f> positions;
//    private final List<Vector2f> texCoords;
//    private final List<Vector3f> normals;
//    private final List<ObjIndex> indices;
//    private boolean hasTexCoords;
//    private boolean hasNormals;

    public ObjLoader() {
//        positions = new ArrayList<>();
//        texCoords = new ArrayList<>();
//        normals = new ArrayList<>();
//        indices = new ArrayList<>();
//        hasTexCoords = false;
//        hasNormals = false;
    }

    public IndexedModel load(String fileName) {
        var vertices = new ArrayList<Vector3f>();
        var textures = new ArrayList<Vector2f>();
        var normals = new ArrayList<Vector3f>();
        var faces = new ArrayList<List<Triple<Integer, Integer, Integer>>>();
        var indices = new ArrayList<Integer>();

        try (var lines = Files.lines(new File(fileName).toPath())) {
            lines
                    .filter(not(Objects::isNull))
                    .filter(line -> !line.startsWith("#"))
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

                            case "f" -> faces.add(Arrays.stream(parts)
                                    .skip(1)
                                    .map(vertexData -> {
                                        var elements = vertexData.split("/");
                                        return new Triple<>(parseInt(elements[0]), parseInt(elements[1]), parseInt(elements[2]));
                                    })
                                    .toList());
                        }
                    });

            var textureArray = new float[vertices.size() * 2];
            var normalArray = new float[vertices.size() * 3];

            faces.forEach(face -> {
                processVertex(face.get(0), indices, textures, normals, textureArray, normalArray);
                processVertex(face.get(1), indices, textures, normals, textureArray, normalArray);
                processVertex(face.get(2), indices, textures, normals, textureArray, normalArray);
            });

            var verticesArray = new float[vertices.size() * 3];
            for (int i = 0; i < vertices.size(); i++) {
                var vertex = vertices.get(i);
                verticesArray[i * 3] = vertex.x;
                verticesArray[i * 3 + 1] = vertex.y;
                verticesArray[i * 3 + 2] = vertex.z;
            }

            var indicesArray = new int[indices.size()];
            for (int i = 0; i < indices.size(); i++) {
                indicesArray[i] = indices.get(i);
            }

            return new IndexedModel(verticesArray, textureArray, normalArray, indicesArray);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load mesh " + fileName, e);
        }
    }

    private void processVertex(
            Triple<Integer, Integer, Integer> faceData,
            List<Integer> indices,
            List<Vector2f> textures,
            List<Vector3f> normals,
            float[] textureArray,
            float[] normalArray
    ) {
        var currentVertexPointer = faceData.e1 - 1;
        indices.add(currentVertexPointer);

        var currentTexture = textures.get(faceData.e2 - 1);
        textureArray[currentVertexPointer * 2] = currentTexture.x;
        textureArray[currentVertexPointer * 2 + 1] = currentTexture.y;

        var currentNormal = normals.get(faceData.e3 - 1);
        normalArray[currentVertexPointer * 2] = currentNormal.x;
        normalArray[currentVertexPointer * 2 + 1] = currentNormal.y;
        normalArray[currentVertexPointer * 2 + 2] = currentNormal.z;
    }


    record Triple<T1, T2, T3>(T1 e1, T2 e2, T3 e3) {}

//    private void processObjLine(String line) {
//        String[] tokens = line.split(" ");
//
//        switch (tokens[0]) {
//            case "v" -> positions.add(new Vector3f(
//                    parseFloat(tokens[1]),
//                    parseFloat(tokens[2]),
//                    parseFloat(tokens[3])));
//
//            case "vt" -> texCoords.add(new Vector2f(
//                    parseFloat(tokens[1]),
//                    1.0f - parseFloat(tokens[2])));
//
//            case "vn" -> normals.add(new Vector3f(
//                        parseFloat(tokens[1]),
//                        parseFloat(tokens[2]),
//                        parseFloat(tokens[3])));
//
//            case "f" -> {
//                for (int i = 0; i < tokens.length - 3; i++) {
//                    indices.add(parseOBJIndex(tokens[1]));
//                    indices.add(parseOBJIndex(tokens[2 + i]));
//                    indices.add(parseOBJIndex(tokens[3 + i]));
//                }
//            }
//        }
//    }
}
