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
    private final List<Vector3f> positions;
    private final List<Vector2f> texCoords;
    private final List<Vector3f> normals;
    private final List<ObjIndex> indices;
    private boolean hasTexCoords;
    private boolean hasNormals;

    public ObjLoader() {
        positions = new ArrayList<>();
        texCoords = new ArrayList<>();
        normals = new ArrayList<>();
        indices = new ArrayList<>();
        hasTexCoords = false;
        hasNormals = false;
    }

    public IndexedModel load(String fileName) {
        try (var lines = Files.lines(new File(fileName).toPath())) {
            lines
                    .filter(not(Objects::isNull))
                    .filter(line -> !line.startsWith("#"))
                    .forEach(this::processObjLine);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load mesh " + fileName, e);
        }

        return toIndexedModel();
    }

    private void processObjLine(String line) {
        String[] tokens = line.split(" ");

        switch (tokens[0]) {
            case "v" -> positions.add(new Vector3f(
                    parseFloat(tokens[1]),
                    parseFloat(tokens[2]),
                    parseFloat(tokens[3])));

            case "vt" -> texCoords.add(new Vector2f(
                    parseFloat(tokens[1]),
                    1.0f - parseFloat(tokens[2])));

            case "vn" -> normals.add(new Vector3f(
                        parseFloat(tokens[1]),
                        parseFloat(tokens[2]),
                        parseFloat(tokens[3])));

            case "f" -> {
                for (int i = 0; i < tokens.length - 3; i++) {
                    indices.add(parseOBJIndex(tokens[1]));
                    indices.add(parseOBJIndex(tokens[2 + i]));
                    indices.add(parseOBJIndex(tokens[3 + i]));
                }
            }
        }
    }

    public IndexedModel toIndexedModel() {
        IndexedModel result = new IndexedModel();
        IndexedModel normalModel = new IndexedModel();
        Map<ObjIndex, Integer> resultIndexMap = new HashMap<>();
        Map<Integer, Integer> normalIndexMap = new HashMap<>();

        for (ObjIndex currentIndex : indices) {
            Vector3f currentPosition = positions.get(currentIndex.getVertexIndex());
            Vector2f currentTexCoord;
            Vector3f currentNormal;

            if (hasTexCoords) {
                currentTexCoord = texCoords.get(currentIndex.getTexCoordIndex());
            } else {
                currentTexCoord = new Vector2f(0, 0);
            }

            if (hasNormals) {
                currentNormal = normals.get(currentIndex.getNormalIndex());
            } else {
                currentNormal = new Vector3f(0, 0, 0);
            }

            Integer modelVertexIndex = resultIndexMap.get(currentIndex);

            if (modelVertexIndex == null) {
                modelVertexIndex = result.getPositions().size();
                resultIndexMap.put(currentIndex, modelVertexIndex);

                result.getPositions().add(currentPosition);
                result.getTexCoords().add(currentTexCoord);
                if (hasNormals)
                    result.getNormals().add(currentNormal);
            }

            var normalModelIndex = normalIndexMap.get(currentIndex.getVertexIndex());
            if (normalModelIndex == null) {
                normalModelIndex = normalModel.getPositions().size();
                normalIndexMap.put(currentIndex.getVertexIndex(), normalModelIndex);

                normalModel.getPositions().add(currentPosition);
                normalModel.getTexCoords().add(currentTexCoord);
                normalModel.getNormals().add(currentNormal);
            }

            result.getIndices().add(modelVertexIndex);
            normalModel.getIndices().add(normalModelIndex);
        }

        return result;
    }

    private ObjIndex parseOBJIndex(String token) {
        String[] values = token.split("/");

        ObjIndex result = new ObjIndex();
        result.setVertexIndex(parseInt(values[0]) - 1);

        if (values.length > 1) {
            if (!values[1].isEmpty()) {
                hasTexCoords = true;
                result.setTexCoordIndex(parseInt(values[1]) - 1);
            }

            if (values.length > 2) {
                hasNormals = true;
                result.setNormalIndex(parseInt(values[2]) - 1);
            }
        }

        return result;
    }
}
