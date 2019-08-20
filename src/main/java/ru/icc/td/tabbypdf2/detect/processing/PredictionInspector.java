package ru.icc.td.tabbypdf2.detect.processing;

import ru.icc.td.tabbypdf2.detect.processing.recognition.StructureRecognizer;
import ru.icc.td.tabbypdf2.detect.processing.refinement.ParagraphRefinement;
import ru.icc.td.tabbypdf2.detect.processing.verification.DiagramVerification;
import ru.icc.td.tabbypdf2.detect.processing.verification.ImageVerification;
import ru.icc.td.tabbypdf2.detect.processing.verification.StructureVerification;
import ru.icc.td.tabbypdf2.detect.processing.verification.Verification;
import ru.icc.td.tabbypdf2.interfaces.Inspector;
import ru.icc.td.tabbypdf2.interfaces.Refinement;
import ru.icc.td.tabbypdf2.model.Prediction;
import ru.icc.td.tabbypdf2.model.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PredictionInspector implements Inspector<Prediction> {
    private final StructureRecognizer recognition = new StructureRecognizer();
    private final List<Verification> verifications = new ArrayList<>();
    private final List<Refinement<Prediction>> refinements = new ArrayList<Refinement<Prediction>>();
    private Table table;

    public PredictionInspector() {
        setAll();
    }

    @Override
    public boolean inspect(Prediction prediction) {
        if (prediction == null) {
            return false;
        }

        recognition.recognize(prediction);

        for (Verification v : verifications) {

            if (!v.verify(prediction)) {
                return false;
            }
        }

        for (Refinement<Prediction> r : refinements) {
            r.refine(prediction);

            if (!prediction.isTruthful()) {
                return false;
            }
        }

        Table table = new Table(prediction.getBlocks(), prediction.getPage(), prediction.getStructure());
        table.setMap(prediction.getMap());
        this.table = table;

        return true;
    }

    private void setAll(){
        // Verifications
        verifications.addAll(Arrays.asList(new StructureVerification(), new ImageVerification(),
                new DiagramVerification()));
        // Refinements
        refinements.add(new ParagraphRefinement());
    }

    public Table getTable() {
        return table;
    }
}