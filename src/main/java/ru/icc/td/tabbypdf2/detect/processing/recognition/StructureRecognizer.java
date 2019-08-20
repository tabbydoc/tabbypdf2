package ru.icc.td.tabbypdf2.detect.processing.recognition;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

public class StructureRecognizer implements Recognition<Prediction> {
    private final ProjectionRecognizer projectionRecognizer = new ProjectionRecognizer();
    private final GraphComposer graphComposer = new GraphComposer();

    @Override
    public void recognize(Prediction prediction) {
        if (prediction.getBlocks().isEmpty()) {
            return;
        }

        projectionRecognizer.recognize(prediction);

        Graph<Block, DefaultWeightedEdge> graph = graphComposer.compose(prediction.getBlocks());
        prediction.setStructure(graph);
    }
}