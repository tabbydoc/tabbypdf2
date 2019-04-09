package ru.icc.td.tabbypdf2.detect.processing.verification;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

public class ParagraphVerification implements Verification {
    private final StructureVerification verification = new StructureVerification();

    @Override
    public boolean verify(Prediction prediction) {
        if (verification.verify(prediction)) {
            ConnectivityInspector<Block, DefaultWeightedEdge> inspector =
                    new ConnectivityInspector<>(prediction.getStructure());

            return !inspector.isConnected();
        } else {
            return false;
        }
    }
}