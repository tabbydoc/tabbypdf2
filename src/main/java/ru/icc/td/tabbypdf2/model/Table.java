package ru.icc.td.tabbypdf2.model;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Table extends Rectangle2D.Float {
    private Page page;
    private List<Block> blocks = new ArrayList<>();
    private Rectangle2D box;
    private Rectangle2D blockBox;

    public Table(Rectangle2D box, Page page) {
        this.box = box;
        this.page = page;
        setBlocks(box);
    }

    public Table(List<Block> blocks, Page page, Rectangle2D box) {
        this.blocks = blocks;
        this.page = page;
        setBlocks(box);
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    private void setBlocks(Rectangle2D box) {
        for (Block block : page.getBlocks()) {
            if (block.intersects(box))
                blocks.add(block);
        }
        setAll();
    }

    public Rectangle2D getBox() {
        return box;
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

        Rectangle2D rectangle2D = new Rectangle2D.Float(minX, minY, maxX - minX, maxY - minY);
        this.blockBox = rectangle2D;
        setRect(rectangle2D.createUnion(box));
    }

    public Page getPage() {
        return page;
    }
}
