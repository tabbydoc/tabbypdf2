package ru.icc.td.tabbypdf2.comp.block.refinement;

import ru.icc.td.tabbypdf2.comp.util.Line2DVerification;
import ru.icc.td.tabbypdf2.interfaces.Refinement;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Page;

import java.awt.geom.Rectangle2D;
import java.util.List;

import static ru.icc.td.tabbypdf2.comp.util.Line2DVerification.Orientation.VERTICAL;

class IsolatedBlocks implements Refinement<Page> {
    private List<Block> blocks;
    private Page page;

    @Override
    public void refine(Page page) {
        blocks = page.getBlocks();
        this.page = page;

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

        double CONST = 0.5d;
        double spaceI = blockI.getAverageSpaceWidth();

        Rectangle2D rectI = new Rectangle2D.Double();
        rectI.setRect(blockI.x - (1 + CONST) * spaceI, blockI.y,
                blockI.width + 2 * (1 + CONST) * spaceI, blockI.height);

        Block blockJ;

        for (int j = 0; j < blocks.size(); j++) {
            blockJ = blocks.get(j);

            if (rectI.intersects(blockJ) && checkAll(blockI, blockJ)) {
                united = true;
                j = -1;

                blocks.remove(blockJ);
                blockI.addWords(blockJ.getWords());

                union(blockI);
            }
        }

        return united;
    }

    private boolean checkAll(Block blockI, Block blockJ) {
        boolean isRuling = Line2DVerification.verify(blockI, blockJ, page.getRulings(), VERTICAL);
        boolean isCursorTrace = Line2DVerification.verify(blockI, blockJ, page.getCursorTraces(), VERTICAL);

        return !isRuling && !isCursorTrace && !blockI.equals(blockJ);
    }
}