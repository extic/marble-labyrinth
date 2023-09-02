package org.extremely.marble;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.joml.Vector3f;

import java.io.FileReader;
import java.util.List;

public class WallCollisionDetector {
    private static final float WALL_DAMPENING_FACTOR = 0.2f;
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
        boolean detected = false;
        for (var wall: walls) {
            if (wall.direction == WallDirection.EAST) {
                if (wall.vertex1.z - before.z > radius || before.z - wall.vertex2.z > radius) {
                    continue;
                }
                if (before.x - wall.vertex1.x >= radius && after.x - wall.vertex1.x <= radius) {
                    after.x = wall.vertex1.x + radius;
                    velocity.x = -velocity.x * WALL_DAMPENING_FACTOR;
                    detected = true;
                }
            }

            if (wall.direction == WallDirection.WEST) {
                if (wall.vertex1.z - before.z > radius || before.z - wall.vertex2.z > radius) {
                    continue;
                }

                if (wall.vertex1.x - before.x >= radius && wall.vertex1.x - after.x <= radius) {
                    after.x = wall.vertex1.x - radius;
                    velocity.x = -velocity.x * WALL_DAMPENING_FACTOR;
                    detected = true;
                }
            }

            if (wall.direction == WallDirection.NORTH) {
                if (wall.vertex1.x - before.x > radius || before.x - wall.vertex2.x > radius) {
                    continue;
                }
                if (wall.vertex1.z - before.z >= radius && wall.vertex1.z - after.z <= radius) {
                    after.z = wall.vertex1.z - radius;
                    velocity.z = -velocity.z * WALL_DAMPENING_FACTOR;
                    detected = true;
                }
            }

            if (wall.direction == WallDirection.SOUTH) {
                if (wall.vertex1.x - before.x > radius || before.x - wall.vertex2.x > radius) {
                    continue;
                }
                if (before.z - wall.vertex1.z >= radius && after.z - wall.vertex1.z <= radius) {
                    after.z = wall.vertex1.z + radius;
                    velocity.z = -velocity.z * WALL_DAMPENING_FACTOR;
                    detected = true;
                }
            }
        }
        return detected;
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
