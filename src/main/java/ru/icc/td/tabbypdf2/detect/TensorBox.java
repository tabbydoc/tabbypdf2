package ru.icc.td.tabbypdf2.detect;

import org.tensorflow.Tensor;


public class TensorBox {

    private float score;
    private String label;

    public float getScore() {
        return score;
    }

    public String getLabel() {
        return label;
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    public TensorBox(float score, String label, float[] box) {
        this.score = score;
        this.label = label;
        minY = box[0];
        minX = box[1];
        maxY = box[2];
        maxX = box[3];
    }

}
