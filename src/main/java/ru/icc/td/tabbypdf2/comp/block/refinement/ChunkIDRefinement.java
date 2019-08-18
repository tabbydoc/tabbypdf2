package ru.icc.td.tabbypdf2.comp.block.refinement;

import ru.icc.td.tabbypdf2.interfaces.Refinement;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Word;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class ChunkIDRefinement implements Refinement<Page> {
    private List<Word> words;
    private List<Block> blocks;

    @Override
    public void refine(Page page) {
        words = new ArrayList<>(page.getWords());
        words.sort(Comparator.comparing(Word::getStartChunkID));
        blocks = page.getBlocks();

        Word wordI;
        Word wordJ;
        Block block;

        for (int i = 0; i < words.size(); i++) {
            wordI = words.get(i);

            block = wordI.getBlock();
            int id1 = wordI.getStartChunkID();

            for (int j = 0; j < words.size(); j++) {
                wordJ = words.get(j);
                int id2 = wordJ.getStartChunkID();

                if (id2 > id1) {
                    break;
                }

                if (id1 == id2 && !wordI.equals(wordJ) &&
                        !wordJ.getBlock().equals(block)) {
                    separate(wordI, wordJ);

                    j--;
                }
            }

            words.remove(wordI);
            i = -1;
        }

        page.addBlocks(blocks);
    }

    private void separate(Word wordI, Word wordJ) {
        if (wordI.getBlock().removeWord(wordI)) {
            blocks.add(new Block(wordI));
        }

        if (wordJ.getBlock().removeWord(wordJ)) {
            blocks.add(new Block(wordJ));
            words.remove(wordJ);
        }
    }
}