package ru.icc.td.tabbypdf2.detect.processing.verification;

import org.jgrapht.alg.connectivity.GabowStrongConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

import java.util.Set;

public class ParagraphVerification implements Verification {
    private final StructureVerification verification = new StructureVerification();

    @Override
    public boolean verify(Prediction prediction) {
        if (verification.verify(prediction)) {
            GabowStrongConnectivityInspector<Block, DefaultWeightedEdge> inspector =
                    new GabowStrongConnectivityInspector<>(prediction.getStructure());
            int t = 0;

            for (Set<Block> set : inspector.stronglyConnectedSets()) {
                if (set.size() == 1) {
                    t++;
                }
            }

            return !inspector.isStronglyConnected() || t > 2;
        } else {
            return false;
        }
    }
}