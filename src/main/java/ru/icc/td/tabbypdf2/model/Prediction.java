package ru.icc.td.tabbypdf2.model;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import ru.icc.td.tabbypdf2.detect.processing.recognition.Projection;
import ru.icc.td.tabbypdf2.detect.processing.recognition.StructureComposer;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Prediction extends Rectangle2D.Double {
    private final Page page;
    private final List<Block> blocks = new ArrayList<>();
    private Graph<Block, DefaultWeightedEdge> structure = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    private boolean isTruthful;
    private Map<Projection.Horizontal, List<Projection.Vertical>> map;
    private List<Projection.Vertical> verticals;

    public Prediction(Rectangle2D prediction, Page page) {
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
                    setBlocks();
                }
            }
        }
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

    public Map<Projection.Horizontal, List<Projection.Vertical>> getMap() {
        return map;
    }

    public void setMap(Map<Projection.Horizontal, List<Projection.Vertical>> map) {
        this.map = map;
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
        updateStructure();
    }

    private void updateStructure() {
        StructureComposer structureComposer = new StructureComposer();
        structureComposer.compose(this);
    }
}