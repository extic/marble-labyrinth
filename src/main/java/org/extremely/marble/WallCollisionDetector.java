package org.extremely.marble;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.joml.Vector3f;

import java.io.FileReader;
import java.util.List;

public class WallCollisionDetector {
    private final List<Wall> walls;

    public WallCollisionDetector() {
        var gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        try (var reader = new FileReader("./walls.json")) {
            walls = gson.fromJson(reader, new TypeToken<List<Wall>>() {}.getType());
        } catch (Exception e) {
            throw new RuntimeException("Cannot initialize wall detector", e);
        }
    }

    public boolean detect(Vector3f before, Vector3f after, Vector3f velocity, float radius) {
        for (var wall: walls) {
            if (wall.direction == WallDirection.EAST && before.x - wall.vertex1.x >= radius && after.x - wall.vertex1.x <= radius) {
                after.x = wall.vertex1.x + radius;
                velocity.x = 0;
                return true;
            }
//            if (wall.direction == WallDirection.WEST) {
////                System.out.println((wall.vertex1.x - before.x) + "\t\t" + (wall.vertex1.x - after.x));
//            }

            if (wall.direction == WallDirection.WEST && wall.vertex1.x - before.x >= radius && wall.vertex1.x - after.x <= radius) {
                after.x = wall.vertex1.x - radius;
                velocity.x = 0;
                return true;
            }
        }
        return false;
    }

    private record Wall(
            Vector3f vertex1,
            Vector3f vertex2,
            WallDirection direction
    ) {}


    private enum WallDirection {
        NORTH, EAST, SOUTH, WEST
    }
}
