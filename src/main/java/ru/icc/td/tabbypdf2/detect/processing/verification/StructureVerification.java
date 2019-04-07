package ru.icc.td.tabbypdf2.detect.processing.verification;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

import java.util.Set;

public class StructureVerification implements Verification {

    @Override
    public boolean verify(Prediction prediction) {
        Graph<Block, DefaultWeightedEdge> graph = prediction.getStructure();

        if (graph == null) {
            return false;
        }

        Set<Block> vertexes = graph.vertexSet();
        Set<DefaultWeightedEdge> edges = graph.edgeSet();

        if (vertexes == null || edges == null) {
            return false;
        }

        return (!vertexes.isEmpty() &&
                !edges.isEmpty());
    }
}
