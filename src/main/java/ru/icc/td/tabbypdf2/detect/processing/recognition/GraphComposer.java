package ru.icc.td.tabbypdf2.detect.processing.recognition;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import ru.icc.td.tabbypdf2.model.Block;

import java.util.ArrayList;
import java.util.List;

class GraphComposer {
    private Graph<Block, DefaultWeightedEdge> graph;
    private List<Block> blocks;
    private List<Projection> projections;
    private List<Block> rootBlocks;

    Graph<Block, DefaultWeightedEdge> compose(List<Block> blocks, List<Projection> projections) {
        setAll(blocks, projections);

        rootBlocks.forEach(block -> {
            graph.addVertex(block);
            addBlocks(block);
        });

        return graph;
    }

    private void addBlocks(Block block) {
        List<Block> neighbours = block.findTheNearestRelatively(blocks, Block.Direction.SOUTH);
        int level = Projection.getLevel(block, projections);

        neighbours.forEach(neighbour -> {
            int level1 = Projection.getLevel(neighbour, projections);
            boolean isContained = graph.addVertex(neighbour);

            DefaultWeightedEdge edge = graph.addEdge(block, neighbour);
            graph.setEdgeWeight(edge, level1 - level);

            if(!isContained) {
                addBlocks(neighbour);
            }
        });
    }

    private void setAll(List<Block> blocks, List<Projection> projections) {
        graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        setBlocks(blocks);
        setRootBlocks();
        setProjections(projections);
    }

    private void setRootBlocks() {
        rootBlocks = new ArrayList<>();
        rootBlocks.addAll(Block.findTheNearestBlocks(null, blocks, Block.Direction.SOUTH));
        blocks.removeAll(rootBlocks);
    }

    private void setBlocks(List<Block> blocks) {
        this.blocks = new ArrayList<>();
        this.blocks.addAll(blocks);
    }

    private void setProjections(List<Projection> projections) {
        this.projections = new ArrayList<>();
        this.projections = projections;
    }
}