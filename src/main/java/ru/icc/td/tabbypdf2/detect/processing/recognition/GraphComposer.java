package ru.icc.td.tabbypdf2.detect.processing.recognition;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import ru.icc.td.tabbypdf2.model.Block;

import java.util.*;

class GraphComposer {
    private Graph<Block, DefaultWeightedEdge> graph = null;
    private List<Block> blocks;
    private List<Projection> projections;
    private List<Block> rootBlocks;

    Graph<Block, DefaultWeightedEdge> compose(List<Block> blocks, List<Projection> projections) {
        if (blocks.isEmpty()) {
            return graph;
        }

        setAll(blocks, projections);

        rootBlocks.forEach(block -> {
            graph.addVertex(block);

            if (!this.blocks.isEmpty()) {
                doNext(block);
            }
        });

        return graph;
    }

    private void doNext(Block block) {
        List<Block> neighbours = block.findNeighbours(blocks);
        int level1 = Projection.getLevel(block, projections);

        for (int i = 0; i < neighbours.size(); i++) {
            Block neighbour = neighbours.get(i);
            int level2 = Projection.getLevel(neighbour, projections);
            boolean isAdded = graph.addVertex(neighbour);

            DefaultWeightedEdge edge = graph.addEdge(block, neighbour);
            graph.setEdgeWeight(edge, level2 - level1);

            if (isAdded) {
                doNext(neighbour);
            }
        }
    }

    private void setAll(List<Block> blocks, List<Projection> projections) throws NoSuchElementException{
        graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        setBlocks(blocks);
        setRootBlocks();
        setProjections(projections);
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

    private void setBlocks(List<Block> blocks) {
        this.blocks = new ArrayList<>();

        if(blocks.isEmpty()) {
            throw new NoSuchElementException();
        }

        this.blocks.addAll(blocks);
    }

    private void setProjections(List<Projection> projections) {
        this.projections = new ArrayList<>();
        this.projections = projections;
    }
}