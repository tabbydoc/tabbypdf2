package ru.icc.td.tabbypdf2.detect.processing.recognition;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import ru.icc.td.tabbypdf2.interfaces.Algorithm;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class GraphAlgorithm implements Algorithm<Prediction> {
    private Graph<Block, DefaultWeightedEdge> graph;
    private List<Block> blocks;
    private List<Block> rootBlocks;

    @Override
    public void start(Prediction prediction) {
        List<Block> blocks = prediction.getBlocks();

        if (blocks.isEmpty()) {
            return;
        }

        setAll(blocks);

        rootBlocks.forEach(block -> {
            graph.addVertex(block);

            if (!this.blocks.isEmpty()) {
                doNext(block);
            }
        });

        prediction.setStructure(graph);
    }

    private void doNext(Block block) {
        List<Block> neighbours = block.findNeighbours(blocks);
        int level1 = Projection.getLevel(block);

        for (Block neighbour : neighbours) {
            int level2 = Projection.getLevel(neighbour);
            boolean isAdded = graph.addVertex(neighbour);

            DefaultWeightedEdge edge = graph.addEdge(block, neighbour);
            graph.setEdgeWeight(edge, level2 - level1);

            if (isAdded) {
                doNext(neighbour);
            }
        }
    }

    private void setAll(List<Block> blocks) {
        graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        this.blocks = new ArrayList<>(blocks);

        setRootBlocks();
    }

    private void setRootBlocks() {
        rootBlocks = new ArrayList<>();

        Block block = getTopmostBlock();

        rootBlocks.addAll(Block.findNeighbours(block, blocks));
        blocks.removeAll(rootBlocks);
    }

    private Block getTopmostBlock() {
        Block block  = new Block();

        double yMax = Collections.max(blocks, Comparator.comparing(Block::getMaxY)).getMaxY();
        double xMin = Collections.min(blocks, Comparator.comparing(Block::getMinX)).getMinX();
        double xMax = Collections.max(blocks, Comparator.comparing(Block::getMaxX)).getMaxX();

        block.setRect(xMin, yMax, xMax - xMin, 0);

        return block;
    }
}