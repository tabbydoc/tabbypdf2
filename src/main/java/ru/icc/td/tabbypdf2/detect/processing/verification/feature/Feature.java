package ru.icc.td.tabbypdf2.detect.processing.verification.feature;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public abstract class Feature {
    public int size;
    protected double[] values;
    protected String[] names;
    protected String prefix;
    protected DescriptiveStatistics ds;

    protected Feature(int size) {
        this.size = size;
        values = new double[size];
        names = new String[size];
        ds = new DescriptiveStatistics();
    }

    public abstract double[] getValues();

    public String[] getNames() {
        String[] fullNames = new String[size];

        for (int i = 0; i < size; i ++) {
            if (prefix.isEmpty()) {
                fullNames[i] = names[i];
            } else {
                fullNames[i] = prefix + '_' + names[i];
            }
        }

        return fullNames;
    }
}
