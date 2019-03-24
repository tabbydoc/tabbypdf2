package ru.icc.td.tabbypdf2.model;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Prediction extends Rectangle2D.Float {
    private Page page;
    private List<Block> blocks = new ArrayList<>();
    private Graph<Block, DefaultWeightedEdge> structure = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    private boolean isTruthful;

    public Prediction(Rectangle2D prediction, Page page){
        this.page = page;
        setRect(prediction);
        setBlocks();
        setTruthful(true);
    }

    private void setBlocks() {
        List<Block> pageBlocks = new ArrayList<>(page.getBlocks());
        Rectangle2D rectangle = getBounds2D();

        for (Block block : pageBlocks) {

            if (block.intersects(rectangle)) {
                Rectangle2D intersection = block.createIntersection(rectangle);
                double square1 = square(intersection);
                double square2 = square(block);

                if (square1/square2 >= 0.5) {
                    blocks.add(block);
                }
            } // end of first condition

        } // end of cycle

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
}