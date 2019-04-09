package ru.icc.td.tabbypdf2.detect.processing.verification;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

public class ParagraphVerification implements Verification {

    @Override
    public boolean verify(Prediction prediction) {
        ConnectivityInspector<Block, DefaultWeightedEdge> inspector =
                new ConnectivityInspector<>(prediction.getStructure());

        return !inspector.isConnected();
    }
}