package org.extremely.engine.core;

import org.extremely.engine.rendering.Vertex;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Utils {
    public static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static FloatBuffer createFloatBuffer(int size) {
        return BufferUtils.createFloatBuffer(size);
    }

    public static IntBuffer createIntBuffer(int size) {
        return BufferUtils.createIntBuffer(size);
    }

    public static ByteBuffer createByteBuffer(int size) {
        return BufferUtils.createByteBuffer(size);
    }

    public static IntBuffer createFlippedBuffer(int... values) {
        IntBuffer buffer = createIntBuffer(values.length);
        buffer.put(values);
        buffer.flip();

        return buffer;
    }

    public static FloatBuffer createFlippedBuffer(Vertex[] vertices) {
        FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.SIZE);

        for (Vertex vertex : vertices) {
            buffer.put(vertex.getPos().x);
            buffer.put(vertex.getPos().y);
            buffer.put(vertex.getPos().z);
            buffer.put(vertex.getTexCoord().x);
            buffer.put(vertex.getTexCoord().y);
            buffer.put(vertex.getNormal().x);
            buffer.put(vertex.getNormal().y);
            buffer.put(vertex.getNormal().z);
        }

        buffer.flip();

        return buffer;
    }
}
