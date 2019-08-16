package ru.icc.td.tabbypdf2.model;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import ru.icc.td.tabbypdf2.detect.processing.recognition.StructureRecognizer;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Prediction extends Rectangle2D.Double {
    private Page page;
    private List<Block> blocks = new ArrayList<>();
    private Graph<Block, DefaultWeightedEdge> structure = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    private boolean isTruthful;

    public Prediction(Rectangle2D prediction, Page page){
        this.page = page;
        setRect(prediction);
        setPageBlocks(page);
        setBlocks();
        setTruthful(true);
    }

    private List<Block> pageBlocks;

    private void setBlocks() {
        Rectangle2D rectangle = getBounds2D();

        for (int i = 0; i < pageBlocks.size(); i++) {
            Block block = pageBlocks.get(i);

            if (block.intersects(rectangle)) {
                Rectangle2D intersection = block.createIntersection(rectangle);
                double square1 = square(intersection);
                double square2 = square(block);

                if (square1 / square2 >= 0.5) {
                    blocks.add(block);
                    pageBlocks.remove(block);
                    i--;
                    setAll();
                    setBlocks();
                }
            }
        }
    }

    private void setAll() {
        double minX = java.lang.Double.MAX_VALUE;
        double minY = java.lang.Double.MAX_VALUE;
        double maxX = java.lang.Double.MIN_VALUE;
        double maxY = java.lang.Double.MIN_VALUE;

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

    private void setPageBlocks(Page page) {
        this.pageBlocks = new ArrayList<>(page.getBlocks());
    }

    private double square(Rectangle2D rectangle){
        return rectangle.getHeight() * rectangle.getWidth();
    }

    public void setStructure(Graph<Block, DefaultWeightedEdge> structure) {
        this.structure = structure;
    }

    public void setTruthful(boolean truthful) {
        isTruthful = truthful;
    }

    public boolean isTruthful() {
        return isTruthful;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public Page getPage() {
        return page;
    }

    public Graph<Block, DefaultWeightedEdge> getStructure() {
        return structure;
    }

    public void removeBlock(Block block) {
        blocks.remove(block);
        setAll();
        updateStructure();
    }

    private void updateStructure() {
        StructureRecognizer structureRecognizer = new StructureRecognizer();
        structureRecognizer.recognize(this);
    }
}