package ru.icc.td.tabbypdf2.model;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import ru.icc.td.tabbypdf2.detect.processing.recognition.Projection;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Table extends Rectangle2D.Double {
    private List<Block> blocks = new ArrayList<>();
    private Page page;
    private Graph<Block, DefaultWeightedEdge> structure = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    private Map<Projection.Horizontal, List<Projection.Vertical>> map;
    private List<Projection.Vertical> verticals = new ArrayList<>();

    public Map<Projection.Horizontal, List<Projection.Vertical>> getMap() {
        return map;
    }

    public Table(List<Block> blocks, Page page, Graph<Block, DefaultWeightedEdge> structure) {
        if (blocks == null)
            return;

        this.blocks = blocks;
        this.page = page;
        this.structure = structure;

        setAll();
    }

    public Table(Prediction prediction) {
        if (prediction == null) {
            return;
        }

        verticals = prediction.getVerticals();
        blocks = prediction.getBlocks();
        structure = prediction.getStructure();
        page = prediction.getPage();
        map = prediction.getMap();

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

    public List<Projection.Horizontal> getHorizontals() {
        return new ArrayList<>(map.keySet());
    }

    public List<Projection.Vertical> getVerticals() {
        return verticals;
    }

    public void setVerticals(List<Projection.Vertical> verticals) {
        this.verticals = verticals;
    }

    public Page getPage() {
        return page;
    }

    public Graph<Block, DefaultWeightedEdge> getStructure() {
        return structure;
    }
}
