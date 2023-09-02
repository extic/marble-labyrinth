package org.extremely.marble;

import com.google.gson.GsonBuilder;
import org.extremely.engine.rendering.loading.ObjLoader;
import org.joml.Vector3f;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

public class WallDataGenerator {
    private void run() {
        var modelsFolder = new File("./res/models");
        var innerWalls = Arrays.stream(requireNonNull(modelsFolder.list((dir, name) -> name.matches("wall\\d+[.]obj"))))
                .flatMap(fileName -> loadWalls("./res/models/" + fileName).stream())
                .toList();

        var walls = new ArrayList<WallData>();
        walls.add(new WallData(new Vector3f(-5.12f, 0, -5.12f), new Vector3f(5.12f, 0, -5.12f), WallDirection.SOUTH));
        walls.add(new WallData(new Vector3f(5.12f, 0, -5.12f), new Vector3f(5.12f, 0, 5.12f), WallDirection.WEST));
        walls.add(new WallData(new Vector3f(-5.12f, 0, 5.12f), new Vector3f(5.12f, 0, 5.12f), WallDirection.NORTH));
        walls.add(new WallData(new Vector3f(-5.12f, 0, -5.12f), new Vector3f(-5.12f, 0, 5.12f), WallDirection.EAST));
        walls.addAll(innerWalls);

        var gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        var json = gson.toJson(walls);

        try {
            Files.writeString(Paths.get("walls.json"), json, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<WallData> loadWalls(String fileName) {
        var loader = new ObjLoader();
        var meshData = loader.load(fileName);

        var triangleVertexIndices = partition(Arrays.stream(meshData.indicesArray()).boxed().toList(), 3);

        return triangleVertexIndices.stream()
                .map(indexList -> {
                    var vertices = indexList.stream()
                            .map(index -> convertToVectors(meshData.verticesArray(), index))
                            .filter(vertex -> vertex.y == 0f)
                            .toList();
                    var normals = indexList.stream().map(index -> convertToVectors(meshData.normalArray(), index)).toList();

                    var calcNormal = new Vector3f().zero().add(normals.get(0)).add(normals.get(1)).add(normals.get(2)).normalize();
                    return new TriangleData(vertices, calcNormal);
                })
                .filter(triangle -> triangle.vertices().size() == 2)
                .map(triangle -> {
                    WallDirection direction = null;
                    var vertex1 = triangle.vertices.get(0);
                    var vertex2 = triangle.vertices.get(1);
                    if (triangle.normal.x == 1f) {
                        direction = WallDirection.EAST;
                    } else if (triangle.normal.x == -1f) {
                        direction = WallDirection.WEST;
                    } else if (triangle.normal.z == 1f) {
                        direction = WallDirection.SOUTH;
                    } else if (triangle.normal.z == -1f) {
                        direction = WallDirection.NORTH;
                    } else {
                        throw new RuntimeException("Could not determine wall direction - " + triangle.normal + ", file=" + fileName);
                    }

                    if (direction == WallDirection.EAST || direction == WallDirection.WEST) {
                        if (vertex1.z > vertex2.z) {
                            var tmp = vertex1;
                            vertex1 = vertex2;
                            vertex2 = tmp;
                        }
                    }

                    if (direction == WallDirection.NORTH || direction == WallDirection.SOUTH) {
                        if (vertex1.x > vertex2.x) {
                            var tmp = vertex1;
                            vertex1 = vertex2;
                            vertex2 = tmp;
                        }
                    }

                    return new WallData(vertex1, vertex2, direction);
                })
                .toList();
    }

    private Vector3f convertToVectors(float[] floats, int index) {
        return new Vector3f(
                floats[index * 3],
                floats[index * 3 + 1],
                floats[index * 3 + 2]
        );
    }

    public static <T> List<List<T>> partition(List<T> collection, int size) {
        return IntStream.iterate(0, i -> i < collection.size(), i -> i + size)
                .mapToObj(i -> collection.subList(i, Math.min(i + size, collection.size())))
                .toList();
    }

    public static void main(String[] args) {
        var app = new WallDataGenerator();
        app.run();
    }

    private record TriangleData(
            List<Vector3f> vertices,
            Vector3f normal
    ) {}

    private record WallData(
            Vector3f vertex1,
            Vector3f vertex2,
            WallDirection direction
    ) {}

    private enum WallDirection {
        NORTH, EAST, SOUTH, WEST
    }
}
