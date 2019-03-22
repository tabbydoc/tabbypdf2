package ru.icc.td.tabbypdf2.detect.processing.recognition;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

import java.util.ArrayList;
import java.util.List;

public class StructureRecognizer
        implements Recognition<Prediction, Prediction> {
    private final ProjectionRecognizer projectionRecognizer = new ProjectionRecognizer();
    private Prediction prediction;
    private List<Projection> projections = new ArrayList<>();
    private Graph<Block, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

    @Override
    public Prediction recognize(Prediction prediction) {
        setAll(prediction);
        prediction.setStructure(graph);

        return prediction;
    }

    private void setAll(Prediction prediction){
        this.prediction = prediction;
        setProjections();
        setGraph();
    }
    
    private void setProjections() {
        List<Block> blocks = prediction.getBlocks();
        this.projections = projectionRecognizer.recognize(blocks);
    }

    private void setGraph(){

    }
}