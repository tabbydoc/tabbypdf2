package ru.icc.td.tabbypdf2.detect.processing.verification.feature;

import ru.icc.td.tabbypdf2.model.Prediction;
import ru.icc.td.tabbypdf2.model.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParameterFactory {
    private final double[] parameters;
    private final List<String> names = new ArrayList<>();
    public int size = 0;
    private int counter;

    public ParameterFactory(Prediction prediction) {
        counter = 0;

        List<Feature> features = new ArrayList<>(Arrays.asList(
                new BlockCounterFeature(prediction), new EdgeFeature(prediction), new CoordinateFeature(prediction),
                new VertexFeature(prediction), new FullnessFeature(prediction), new ConnectivityFeature(prediction),
                new AlignFeature(prediction)));

        for (Feature feature: features) {
            size = size + feature.size;
            names.addAll(Arrays.asList(feature.names));
        }
        parameters = new double[size];

        features.forEach(feature -> setElements(feature.getValues()));
    }

    public ParameterFactory(Table table) {
        counter = 0;

        List<Feature> features = new ArrayList<>(Arrays.asList(
                new BlockCounterFeature(table), new EdgeFeature(table), new CoordinateFeature(table),
                new VertexFeature(table), new FullnessFeature(table), new ConnectivityFeature(table),
                new AlignFeature(table)));

        for (Feature feature: features) {
            size = size + feature.size;
            names.addAll(Arrays.asList(feature.getNames()));
        }
        parameters = new double[size];

        features.forEach(feature -> setElements(feature.getValues()));
    }

    public double[] getParameters() {
        return parameters;
    }

    private void setElement(double p) {
        parameters[counter] = p;
        counter++;
    }

    public String[] getNames() {
        return names.toArray(new String[0]);
    }

    private void setElements(double[] values) {
        for (double d: values) {
            if (Double.isNaN(d)) {
                setElement(0);
            } else {
                setElement(d);
            }
        }
    }
}
