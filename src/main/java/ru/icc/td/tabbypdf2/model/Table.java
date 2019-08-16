package ru.icc.td.tabbypdf2.model;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Table extends Rectangle2D.Double {
    private List<Block> blocks = new ArrayList<>();
    private Page page;
    private Graph<Block, DefaultWeightedEdge> structure = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

    public Table(List<Block> blocks, Page page, Graph<Block, DefaultWeightedEdge> structure) {
        if (blocks == null)
            return;

        this.blocks = blocks;
        this.page = page;
        this.structure = structure;

        setAll();
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

    public Page getPage() {
        return page;
    }

    public Graph<Block, DefaultWeightedEdge> getStructure() {
        return structure;
    }
}
