package ru.icc.td.tabbypdf2.detect.processing.verification.feature;

import ru.icc.td.tabbypdf2.model.Prediction;
import ru.icc.td.tabbypdf2.model.Table;

public class BlockCounterFeature extends Feature {

    public BlockCounterFeature(Table table) {
        super(1);

        prefix = "block";
        names[0] = "count";
        values[0] = table.getBlocks().size();
    }

    public BlockCounterFeature(Prediction prediction) {
        super(1);

        prefix = "block";
        names[0] = "count";
        values[0] = prediction.getBlocks().size();
    }

    @Override
    public double[] getValues() {
        return values;
    }
}
