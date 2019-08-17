package ru.icc.td.tabbypdf2.comp.block.refinement;

import ru.icc.td.tabbypdf2.interfaces.Refinement;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Page;

import java.util.List;

class IntersectedBlocks implements Refinement<Page> {
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
        Block blockJ;
        boolean changed = false;

        for (int j = 0; j < blocks.size(); j++) {
            blockJ = blocks.get(j);

            if (blockI.intersects(blockJ) && !blockI.equals(blockJ)) {
                blocks.remove(blockJ);
                blockI.addWords(blockJ.getWords());

                changed = true;
                j--;

                union(blockI);
            }
        }
        return changed;
    }
}