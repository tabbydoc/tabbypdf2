package ru.icc.td.tabbypdf2.detect.processing.verification;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.interfaces.Verification;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

public class DiagramVerification implements Verification {
    private final double[] parameters = new double[35];
    private int counter;
    private DescriptiveStatistics ds = new DescriptiveStatistics();
    private DescriptiveStatistics dsX = new DescriptiveStatistics();
    private DescriptiveStatistics dsY = new DescriptiveStatistics();

    @Override
    public boolean verify(Prediction prediction) {
        counter = 0;

        setElement(prediction.getMaxY());
        setElement(prediction.getBlocks().size());

        Graph<Block, DefaultWeightedEdge> structure = prediction.getStructure();

        for (DefaultWeightedEdge edge : structure.edgeSet()) {
            ds.addValue(structure.getEdgeWeight(edge));
        }

        writeStat(ds);

        for (Block block : prediction.getBlocks()) {
            dsX.addValue(block.getCenterX());
            dsY.addValue(block.getCenterY());
            ds.addValue(structure.degreeOf(block));
        }

        writeStat(dsX);
        writeStat(dsY);
        writeStat(ds);

        KosarajuStrongConnectivityInspector<Block, DefaultWeightedEdge> inspector =
                new KosarajuStrongConnectivityInspector<>(structure);

        setElement(inspector.stronglyConnectedSets().size());

        double[] r = Model.score(parameters);

        return r[1] >= 0.50; // Parameter should be tested
    }

    private void setElement(double p) {
        parameters[counter] = p;
        counter++;
    }

    private void writeStat(DescriptiveStatistics ds) {
        setElement(ds.getSum());
        setElement(ds.getN());
        setElement(ds.getMean());
        setElement(ds.getMax());
        setElement(ds.getMin());
        setElement(ds.getPercentile(50));
        setElement(ds.getStandardDeviation());
        setElement(ds.getVariance());
        ds.clear();
    }
}