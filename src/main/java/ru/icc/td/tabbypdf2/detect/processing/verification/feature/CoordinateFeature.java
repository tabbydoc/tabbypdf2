package ru.icc.td.tabbypdf2.detect.processing.verification.feature;

import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;
import ru.icc.td.tabbypdf2.model.Table;

import java.awt.geom.RectangularShape;
import java.util.List;

public class CoordinateFeature extends Feature {

    public CoordinateFeature(Table table) {
        super(10);
        prefix = "";
        names[0] = "xmean";     names[5] = "ymean";
        names[1] = "xmax";      names[6] = "ymax";
        names[2] = "xmin";      names[7] = "ymin";
        names[3] = "xmedian";   names[8] = "ymedian";
        names[4] = "xsd";       names[9] = "ysd";

        process(table.getBlocks(), RectangularShape::getCenterX);
        putIntoValues(0);
        process(table.getBlocks(), RectangularShape::getCenterY);
        putIntoValues(5);
    }

    public CoordinateFeature(Prediction prediction) {
        super(10);
        prefix = "";
        names[0] = "xmean";     names[5] = "ymean";
        names[1] = "xmax";      names[6] = "ymax";
        names[2] = "xmin";      names[7] = "ymin";
        names[3] = "xmedian";   names[8] = "ymedian";
        names[4] = "xsd";       names[9] = "ysd";

        process(prediction.getBlocks(), RectangularShape::getCenterX);
        putIntoValues(0);
        process(prediction.getBlocks(), RectangularShape::getCenterY);
        putIntoValues(5);
    }

    private void process(List<Block> blocks, BlockHandler method) {
        blocks.forEach(block -> ds.addValue(method.getCenter(block)));
    }

    private void putIntoValues(int start) {
        values[start] = ds.getMean();
        values[start + 1] = ds.getMax();
        values[start + 2] = ds.getMin();
        values[start + 3] = ds.getPercentile(50);
        values[start + 4] = ds.getStandardDeviation();

        ds.clear();
    }

    @Override
    public double[] getValues() {
        return values;
    }

    protected interface BlockHandler {
        double getCenter(Block block);
    }
}
