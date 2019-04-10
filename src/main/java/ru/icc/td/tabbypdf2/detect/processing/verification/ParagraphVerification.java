package ru.icc.td.tabbypdf2.detect.processing.verification;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

import java.util.Set;

public class ParagraphVerification implements Verification {
    private final StructureVerification verification = new StructureVerification();

    //TODO: check amount of 1-size sets
    @Override
    public boolean verify(Prediction prediction) {
        if (verification.verify(prediction)) {
            ConnectivityInspector<Block, DefaultWeightedEdge> inspector =
                    new ConnectivityInspector<>(prediction.getStructure());
            int t = 0;

            for (Set<Block> set : inspector.connectedSets()) {
                if (set.size() == 1) {
                    t++;
                }
            }

            return !inspector.isConnected() && t <= 2;
        } else {
            return false;
        }
    }
}