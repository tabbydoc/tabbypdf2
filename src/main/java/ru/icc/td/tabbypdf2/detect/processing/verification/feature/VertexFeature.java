package ru.icc.td.tabbypdf2.detect.processing.verification.feature;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;
import ru.icc.td.tabbypdf2.model.Table;

public class VertexFeature extends Feature {

    public VertexFeature(Table table) {
        super(6);
        prefix = "vertex";
        names[0] = "sum";
        names[1] = "mean";
        names[2] = "max";
        names[3] = "min";
        names[4] = "median";
        names[5] = "sd";

        Graph<Block, DefaultWeightedEdge> structure = table.getStructure();

        for (Block block : table.getStructure().vertexSet()) {
            ds.addValue(structure.degreeOf(block));
        }
    }

    public VertexFeature(Prediction prediction) {
        super(6);
        prefix = "vertex";
        names[0] = "sum";
        names[1] = "mean";
        names[2] = "max";
        names[3] = "min";
        names[4] = "median";
        names[5] = "sd";
        Graph<Block, DefaultWeightedEdge> structure = prediction.getStructure();

        for (Block block : prediction.getStructure().vertexSet()) {
            ds.addValue(structure.degreeOf(block));
        }
    }

    @Override
    public double[] getValues() {
        values[0] = ds.getSum();
        values[1] = ds.getMean();
        values[2] = ds.getMax();
        values[3] = ds.getMin();
        values[4] = ds.getPercentile(50);
        values[5] = ds.getStandardDeviation();

        return values;
    }
}
