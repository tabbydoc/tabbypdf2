package ru.icc.td.tabbypdf2.detect.processing.verification;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

public class DiagramVerification implements Verification {

    @Override
    public boolean verify(Prediction prediction) {
        Graph<Block, DefaultWeightedEdge> structure = prediction.getStructure();
        DescriptiveStatistics ds = new DescriptiveStatistics();

        for (DefaultWeightedEdge edge : structure.edgeSet()) {
            ds.addValue(structure.getEdgeWeight(edge));
        }
        //System.out.printf("Mean %f, Page %d\n", ds.getMean(), prediction.getPage().getIndex());
        return ds.getMean() <= 1.7f && ds.getMean() >= 0.5f;
    }
}
