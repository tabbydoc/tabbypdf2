package ru.icc.td.tabbypdf2.detect.processing.verification.feature;

import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;
import ru.icc.td.tabbypdf2.model.Table;

public class FullnessFeature extends Feature {

    public FullnessFeature(Table table) {
        super(1);
        prefix = "";
        names[0] = "relation";

        for (Block block : table.getBlocks()) {
            ds.addValue(block.width * block.height);
        }

        values[0] = ds.getSum() / (table.width * table.height);
    }

    public FullnessFeature(Prediction prediction) {
        super(1);
        prefix = "relation";
        names[0] = "";

        for (Block block : prediction.getBlocks()) {
            ds.addValue(block.width * block.height);
        }

        values[0] = ds.getSum() / (prediction.width * prediction.height);
    }

    @Override
    public double[] getValues() {
        return values;
    }
}
