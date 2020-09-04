package ru.icc.td.tabbypdf2.detect.processing.verification;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.interfaces.Verification;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

public class DiagramVerification implements Verification {
    private final double[] parameters = new double[38];
    private int counter;
    private final DescriptiveStatistics ds = new DescriptiveStatistics();
    private final DescriptiveStatistics dsX = new DescriptiveStatistics();
    private final DescriptiveStatistics dsY = new DescriptiveStatistics();

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

        ds.clear();
        double sum = 0;

        for (Block block : prediction.getBlocks()) {
            dsX.addValue(block.getCenterX());
            dsY.addValue(block.getCenterY());
            ds.addValue(structure.degreeOf(block));
            sum = sum + (block.width * block.height);
        }

        double relation = sum / (prediction.width * prediction.height);
        setElement(relation);
        writeStat(dsX);
        setElement(prediction.getCenterX());
        writeStat(dsY);
        setElement(prediction.getCenterY());
        writeStat(ds);

        KosarajuStrongConnectivityInspector<Block, DefaultWeightedEdge> inspector =
                new KosarajuStrongConnectivityInspector<>(structure);

        setElement(inspector.stronglyConnectedSets().size());

        double[] r = Model.score(parameters);

        return r[1] >= 0.50;
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