package ru.icc.td.tabbypdf2.model;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Table extends Rectangle2D.Float {
    private List<Block> blocks = new ArrayList<>();

    public Table(List<Block> blocks) {
        if (blocks == null)
            return;

        this.blocks = blocks;

        setAll();
    }

    private void setAll() {
        float minX = java.lang.Float.MAX_VALUE;
        float minY = java.lang.Float.MAX_VALUE;
        float maxX = java.lang.Float.MIN_VALUE;
        float maxY = java.lang.Float.MIN_VALUE;

        for (Block block : blocks) {
            if (block.x < minX)
                minX = block.x;

            if (block.x + block.width > maxX)
                maxX = block.x + block.width;

            if (block.y < minY)
                minY = block.y;

            if (block.y + block.height > maxY)
                maxY = block.y + block.height;
        }

        setRect(minX, minY, maxX - minX, maxY - minY);
    }
}
