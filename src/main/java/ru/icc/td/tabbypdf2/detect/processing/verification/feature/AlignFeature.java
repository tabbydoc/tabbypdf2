package ru.icc.td.tabbypdf2.detect.processing.verification.feature;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;
import ru.icc.td.tabbypdf2.model.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AlignFeature extends Feature {
    private final Graph<Block, DefaultWeightedEdge> graph;

    public AlignFeature(Table table) {
        super(1);
        prefix = "align";
        names[0] = "value";
        graph = table.getStructure();

        List<Block> roots = findRoots();
        process(roots);
    }

    public AlignFeature(Prediction prediction) {
        super(1);
        prefix = "align";
        names[0] = "value";
        graph = prediction.getStructure();

        List<Block> roots = findRoots();
        process(roots);
    }

    private void process(List<Block> blocks) {
        for (Block current : blocks) {
            Set<DefaultWeightedEdge> edges = graph.outgoingEdgesOf(current);
            List<Block> adjacentBlocks = getAdjacent(current);

            if (edges.size() > 1) {
                process(adjacentBlocks);
                continue;
            } else if (edges.size() == 0) {
                break;
            }

            Block adjacent = adjacentBlocks.get(0);

            if (graph.outgoingEdgesOf(adjacent).size() == 1) {
                if (isAligned(adjacent.getMinX(), current.getMinX()) ||
                        isAligned(adjacent.getCenterX(), current.getCenterX()) ||
                        isAligned(adjacent.getMaxX(), current.getMaxX())) {

                    ds.addValue(1);
                } else {
                    ds.addValue(0);
                }
            }

            process(adjacentBlocks);
        }
    }

    private List<Block> findRoots() {
        List<Block> blocks = new ArrayList<>();

        for (Block block: graph.vertexSet()) {
            if (graph.inDegreeOf(block) == 0) {
                blocks.add(block);
            }
        }

        return blocks;
    }

    private List<Block> getAdjacent(Block block) {
        List<Block> blocks = new ArrayList<>();
        Set<DefaultWeightedEdge> edges = graph.outgoingEdgesOf(block);

        for (DefaultWeightedEdge edge: edges) {
            Block target = graph.getEdgeTarget(edge);
            blocks.add(target);
        }

        return blocks;
    }

    private boolean isAligned(double x, double y) {
        double epsilon = 5;

        return Math.abs(x - y) <= epsilon;
    }

    @Override
    public double[] getValues() {
        values[0] = ds.getMean();
        return values;
    }
}
