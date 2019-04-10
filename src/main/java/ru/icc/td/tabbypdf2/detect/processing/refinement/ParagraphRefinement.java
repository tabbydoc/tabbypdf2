package ru.icc.td.tabbypdf2.detect.processing.refinement;

import ru.icc.td.tabbypdf2.detect.processing.verification.ParagraphVerification;
import ru.icc.td.tabbypdf2.detect.processing.verification.StructureVerification;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

import java.util.Collections;
import java.util.Comparator;

public class ParagraphRefinement implements Refinement {
    private final ParagraphVerification paragraphVerification = new ParagraphVerification();
    private final StructureVerification structureVerification = new StructureVerification();

    @Override
    public Prediction refine(Prediction prediction) {

        while (!paragraphVerification.verify(prediction)) {

            if (structureVerification.verify(prediction)) {
                removeTopmost(prediction);
            } else {
                prediction.setTruthful(false);
                return prediction;
            }
        }

        if (structureVerification.verify(prediction)) {
            prediction.setTruthful(true);
            return prediction;
        }

        prediction.setTruthful(false);
        return prediction;
    }

    private void removeTopmost(Prediction prediction) {
        Block block = new Block();

        double yMax = Collections.max(prediction.getBlocks(), Comparator.comparing(Block::getMaxY)).getMaxY();
        double xMin = Collections.min(prediction.getBlocks(), Comparator.comparing(Block::getMinX)).getMinX();
        double xMax = Collections.max(prediction.getBlocks(), Comparator.comparing(Block::getMaxX)).getMaxX();

        block.setRect(xMin, yMax, xMax - xMin, 0);
        Block root = Block.findNeighbours(block, prediction.getBlocks()).get(0);
        prediction.removeBlock(root);

        Block block1 = Collections.min(prediction.getBlocks(), Comparator.comparingDouble(Block::getMinY));
        if (prediction.getStructure().inDegreeOf(block1) > 1 && prediction.getStructure().outDegreeOf(block1) == 0) {
            prediction.removeBlock(block1);
        }
    }
}