package ru.icc.td.tabbypdf2.detect.processing.recognition;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

import java.util.ArrayList;
import java.util.List;

public class StructureRecognizer implements Recognition<Prediction, Prediction> {
    private final ProjectionRecognizer projectionRecognizer = new ProjectionRecognizer();
    private final GraphComposer graphComposer = new GraphComposer();
    private Prediction prediction;
    private List<Projection> projections;
    private List<Block> blocks;

    @Override
    public Prediction recognize(Prediction prediction) {
        setAll(prediction);
        return prediction;
    }

    private void setAll(Prediction prediction){
        this.prediction = prediction;
        setBlocks();
        setProjections();
        setGraph();
    }

    private void setBlocks() {
        blocks = new ArrayList<>();
        blocks.addAll(prediction.getBlocks());
    }

    private void setProjections() {
        projections = new ArrayList<>();
        projections = projectionRecognizer.recognize(blocks);
    }

    private void setGraph(){
        Graph<Block, DefaultWeightedEdge> graph = graphComposer.compose(blocks, projections);
        prediction.setStructure(graph);
    }
}