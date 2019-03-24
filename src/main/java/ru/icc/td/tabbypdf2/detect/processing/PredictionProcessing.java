package ru.icc.td.tabbypdf2.detect.processing;

import ru.icc.td.tabbypdf2.detect.processing.recognition.StructureRecognizer;
import ru.icc.td.tabbypdf2.detect.processing.refinement.ParagraphRefinement;
import ru.icc.td.tabbypdf2.detect.processing.refinement.Refinement;
import ru.icc.td.tabbypdf2.detect.processing.verification.ImageVerification;
import ru.icc.td.tabbypdf2.detect.processing.verification.ParagraphVerification;
import ru.icc.td.tabbypdf2.detect.processing.verification.Verification;
import ru.icc.td.tabbypdf2.model.Prediction;
import ru.icc.td.tabbypdf2.model.Table;

import java.util.ArrayList;
import java.util.List;

public class PredictionProcessing {
    private final StructureRecognizer recognition = new StructureRecognizer();
    private final TableComposer tableComposer = new TableComposer();
    private final List<Verification> verifications = new ArrayList<>();
    private final List<Refinement> refinements = new ArrayList<>();
    private Table table = null;

    public PredictionProcessing() {
        setAll();
    }

    public boolean isTable(Prediction prediction) {
        prediction = recognition.recognize(prediction);

        for (Verification v : verifications) {

            if (!v.verify(prediction)) {
                return false;
            }
        }

        for (Refinement r : refinements) {
            prediction = r.refine(prediction);

            if (!prediction.isTruthful()) {
                return false;
            }
        }

        this.table = tableComposer.compose(prediction);

        return true;
    }

    private void setAll(){
        // Verifications
        verifications.add(new ImageVerification());
        verifications.add(new ParagraphVerification());
        // Refinements
        refinements.add(new ParagraphRefinement());
    }

    public Table getTable() {
        return table;
    }
}