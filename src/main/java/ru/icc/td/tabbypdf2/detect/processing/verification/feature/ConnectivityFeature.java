package ru.icc.td.tabbypdf2.detect.processing.verification.feature;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;
import ru.icc.td.tabbypdf2.model.Table;

public class ConnectivityFeature extends Feature {

    public ConnectivityFeature(Table table) {
        super(1);
        prefix = "connectivitySets";
        names[0] = "count";

        Graph<Block, DefaultWeightedEdge> structure = table.getStructure();

        KosarajuStrongConnectivityInspector<Block, DefaultWeightedEdge> inspector =
                new KosarajuStrongConnectivityInspector<>(structure);

        values[0] = inspector.stronglyConnectedSets().size();
    }

    public ConnectivityFeature(Prediction prediction) {
        super(1);
        prefix = "connectivitySets";
        names[0] = "count";

        Graph<Block, DefaultWeightedEdge> structure = prediction.getStructure();

        KosarajuStrongConnectivityInspector<Block, DefaultWeightedEdge> inspector =
                new KosarajuStrongConnectivityInspector<>(structure);

        values[0] = inspector.stronglyConnectedSets().size();
    }

    @Override
    public double[] getValues() {
        return values;
    }
}
