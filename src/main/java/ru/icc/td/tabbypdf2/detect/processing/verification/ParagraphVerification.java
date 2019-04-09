package ru.icc.td.tabbypdf2.detect.processing.verification;

import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Prediction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ParagraphVerification implements Verification {
    /* TODO: connectivity analysis */

    private List<Block> blocks;
    @Override
    public boolean verify(Prediction prediction) {
        blocks = new ArrayList<>(prediction.getBlocks());
        Block block = getTopmostBlock();

        return Block.findNeighbours(block, blocks).size() > 1;
    }

    private Block getTopmostBlock() {
        Block block = new Block();

        double yMax = Collections.max(blocks, Comparator.comparing(Block::getMaxY)).getMaxY();
        double xMin = Collections.min(blocks, Comparator.comparing(Block::getMinX)).getMinX();
        double xMax = Collections.max(blocks, Comparator.comparing(Block::getMaxX)).getMaxX();

        block.setRect(xMin, yMax, xMax - xMin, 0);

        return block;
    }
}