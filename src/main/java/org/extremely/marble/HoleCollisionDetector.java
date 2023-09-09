package org.extremely.marble;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.joml.Vector3f;

import java.io.FileReader;
import java.util.List;

public class HoleCollisionDetector {
    private List<Vector3f> holes;

    public HoleCollisionDetector() {
        var gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        try (var reader = new FileReader("./holes.json")) {
            var holePositions = (List<Hole>)gson.fromJson(reader, new TypeToken<List<Hole>>() {}.getType());
            holes = holePositions.stream()
                    .map(it -> {
                        var x = (it.x - 512f) / 1024f * 10f;
                        var z = (it.z - 512f) / 1024f * 10f;
                        return new Vector3f(x, 0, z);
                    })
                    .toList();;
        } catch (Exception e) {
            throw new RuntimeException("Cannot initialize holes detector", e);
        }
    }

    public Vector3f detect(Vector3f pos, float radius) {
        for (Vector3f hole : holes) {
            if (pos.distance(hole) < radius) {
                return hole;
            }
        }
        return null;
    }

    private record Hole(float x, float z) {}
}
