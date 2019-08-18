package ru.icc.td.tabbypdf2.comp.block.refinement;

import ru.icc.td.tabbypdf2.interfaces.Refinement;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Page;

import java.awt.geom.Rectangle2D;
import java.util.List;

class IsolatedBlocks implements Refinement<Page> {
    private List<Block> blocks;
    private Page page;

    @Override
    public void refine(Page page) {
        setAll(page);

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

        double spaceI = blockI.getMaxSpaceWidth();
        Rectangle2D rectI = new Rectangle2D.Double();
        rectI.setRect(blockI.x - 1.5 * spaceI, blockI.y,
                blockI.width + 3 * spaceI, blockI.height);

        Block blockJ;
        double spaceJ;
        Rectangle2D rectJ = new Rectangle2D.Double();

        for (int j = 0; j < blocks.size(); j++) {
            blockJ = blocks.get(j);
            spaceJ = blockJ.getMaxSpaceWidth();
            rectJ.setRect(blockJ.x - spaceJ, blockJ.y,
                    blockJ.width + 2 * spaceJ, blockJ.height);

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

    /*private boolean checkAll(Block blockI, Block blockJ) {
        boolean isRuling = Line2DVerification.verify(blockI, blockJ, page.getRulings(), VERTICAL);
        boolean isCursorTrace = Line2DVerification.verify(blockI, blockJ, page.getCursorTraces(), VERTICAL);

        return !isRuling && !isCursorTrace && !blockI.equals(blockJ);
    }*/

    private void setAll(Page page) {
        blocks = page.getBlocks();
        this.page = page;
    }
}
