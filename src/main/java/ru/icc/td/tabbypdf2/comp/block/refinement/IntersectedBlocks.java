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

        for (int i = 0; i < blocks.size(); i++) {
            Block blockI = blocks.get(i);

            if (union(blockI)) {
                i = -1;
            }
        }
    }

    private boolean union(Block blockI) {
        boolean changed = false;

        for (int j = 0; j < blocks.size(); j++) {
            Block blockJ = blocks.get(j);

            if (blockI.intersects(blockJ) && !blockI.equals(blockJ)) {
                changed = true;
                j--;

                blocks.remove(blockJ);
                blockI.addWords(blockJ.getWords());

                union(blockI);
            }
        }
        return changed;
    }
}