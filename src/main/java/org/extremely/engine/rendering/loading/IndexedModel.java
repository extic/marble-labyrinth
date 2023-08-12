package org.extremely.engine.rendering.loading;

import java.util.List;

public class IndexedModel {
    private final float[] verticesArray;
    private final float[] textureArray;
    private final float[] normalArray;
    private final int[] indicesArray;


    public IndexedModel(List<Float> verticesArray, List<Float> textureArray, List<Float> normalArray, List<Integer> indicesArray) {
        this.verticesArray = toFloatArray(verticesArray);
        this.textureArray = toFloatArray(textureArray);
        this.normalArray = toFloatArray(normalArray);
        this.indicesArray = toIntArray(indicesArray);
    }

    private float[] toFloatArray(List<Float> list) {
        var arr = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    private int[] toIntArray(List<Integer> list) {
        var arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }


    public float[] getVerticesArray() {
        return verticesArray;
    }

    public float[] getTextureArray() {
        return textureArray;
    }

    public float[] getNormalArray() {
        return normalArray;
    }

    public int[] getIndicesArray() {
        return indicesArray;
    }
}
