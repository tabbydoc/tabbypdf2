package ru.icc.td.tabbypdf2.detect.processing;

import ru.icc.td.tabbypdf2.detect.processing.recognition.StructureComposer;
import ru.icc.td.tabbypdf2.detect.processing.verification.DiagramVerification;
import ru.icc.td.tabbypdf2.detect.processing.verification.ImageVerification;
import ru.icc.td.tabbypdf2.detect.processing.verification.StructureVerification;
import ru.icc.td.tabbypdf2.interfaces.Processing;
import ru.icc.td.tabbypdf2.interfaces.Refinement;
import ru.icc.td.tabbypdf2.interfaces.Verification;
import ru.icc.td.tabbypdf2.model.Prediction;
import ru.icc.td.tabbypdf2.model.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PredictionProcessing implements Processing<Prediction> {
    private final StructureComposer recognition = new StructureComposer();
    private final List<Verification> verifications = new ArrayList<>();
    private final List<Refinement<Prediction>> refinements = new ArrayList<>();
    private Table table;
    public boolean isTable;

    public PredictionProcessing(boolean config) {
        if (config) {
            setAll();
        }
    }

    public PredictionProcessing(List<Verification> verifications) {
        this.verifications.addAll(verifications);
    }

    @Override
    public void process(Prediction prediction) {
        if (prediction == null) {
            isTable = false;
            return;
        }

        table = null;

        recognition.compose(prediction);

        for (Verification v : verifications) {

            if (!v.verify(prediction)) {
                isTable = false;
                return;
            }
        }

        for (Refinement<Prediction> r : refinements) {
           r.refine(prediction);

            if (!prediction.isTruthful()) {
                isTable = false;
                return;
            }
        }

        table = new Table(prediction);

        isTable = true;
    }

    private void setAll(){
        verifications.addAll(Arrays.asList(new StructureVerification(),
                new ImageVerification(), new DiagramVerification()));
    }

    public Table getTable() {
        return table;
    }
}