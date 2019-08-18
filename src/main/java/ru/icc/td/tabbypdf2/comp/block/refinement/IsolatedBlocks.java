package ru.icc.td.tabbypdf2.comp.block.refinement;

import ru.icc.td.tabbypdf2.interfaces.Refinement;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Page;

import java.awt.geom.Rectangle2D;
import java.util.List;

class IsolatedBlocks implements Refinement<Page> {
    private List<Block> blocks;

    @Override
    public void refine(Page page) {
        blocks = page.getBlocks();

        Block blockI;

        for (int i = 0; i < blocks.size(); i++) {
            blockI = blocks.get(i);

            if (union(blockI)) {
                i = -1;
            }
        }
    }

    private boolean union(Block blockI) {
        boolean united = false;

        double CONST = 0.75d;
        double spaceI = blockI.getAverageSpaceWidth();

        Rectangle2D rectI = new Rectangle2D.Double();
        rectI.setRect(blockI.x - (1 + CONST) * spaceI, blockI.y,
                blockI.width + 2 * (1 + CONST) * spaceI, blockI.height);

        Block blockJ;

        for (int j = 0; j < blocks.size(); j++) {
            blockJ = blocks.get(j);

            if (rectI.intersects(blockJ) && !blockI.equals(blockJ)) {
                united = true;
                j = -1;

                blocks.remove(blockJ);
                blockI.addWords(blockJ.getWords());

                union(blockI);
            }
        }

        return united;
    }
}