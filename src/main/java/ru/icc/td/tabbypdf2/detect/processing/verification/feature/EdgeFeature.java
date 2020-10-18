package ru.icc.td.tabbypdf2.detect.processing.verification.feature;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;
import ru.icc.td.tabbypdf2.model.Table;

public class EdgeFeature extends Feature {

    public EdgeFeature(Table table) {
        super(7);
        prefix = "edge";
        names[0] = "sum";
        names[1] = "count";
        names[2] = "mean";
        names[3] = "max";
        names[4] = "min";
        names[5] = "median";
        names[6] = "sd";
        
        Graph<Block, DefaultWeightedEdge> structure = table.getStructure();

        for (DefaultWeightedEdge edge : structure.edgeSet()) {
            ds.addValue(structure.getEdgeWeight(edge));
        }
    }

    public EdgeFeature(Prediction prediction) {
        super(7);
        prefix = "edge";
        names[0] = "sum";
        names[1] = "count";
        names[2] = "mean";
        names[3] = "max";
        names[4] = "min";
        names[5] = "median";
        names[6] = "sd";

        Graph<Block, DefaultWeightedEdge> structure = prediction.getStructure();

        for (DefaultWeightedEdge edge : structure.edgeSet()) {
            ds.addValue(structure.getEdgeWeight(edge));
        }
    }

    @Override
    public double[] getValues() {
        values[0] = ds.getSum();
        values[1] = ds.getN();
        values[2] = ds.getMean();
        values[3] = ds.getMax();
        values[4] = ds.getMin();
        values[5] = ds.getPercentile(50);
        values[6] = ds.getStandardDeviation();

        return values;
    }
}
