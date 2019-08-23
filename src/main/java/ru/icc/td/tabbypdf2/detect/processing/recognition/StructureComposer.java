package ru.icc.td.tabbypdf2.detect.processing.recognition;

import ru.icc.td.tabbypdf2.interfaces.Composer;
import ru.icc.td.tabbypdf2.model.Prediction;

public class StructureComposer implements Composer<Prediction> {
    private final ProjectionAlgorithm projectionAlgorithm = new ProjectionAlgorithm();
    private final GraphAlgorithm graphAlgorithm = new GraphAlgorithm();

    @Override
    public void compose(Prediction prediction) {
        if (prediction.getBlocks().isEmpty()) {
            return;
        }

        projectionAlgorithm.start(prediction);
        graphAlgorithm.start(prediction);
    }
}