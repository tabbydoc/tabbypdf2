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

        for (int i = 0; i < words.size(); i++) {
            Word wordI = words.get(i);

            Block block = wordI.getBlock();
            int id1 = wordI.getStartChunkID();

            for (int j = 0; j < words.size(); j++) {
                Word wordJ = words.get(j);
                int id2 = wordJ.getStartChunkID();

                if (id2 > id1) {
                    break;
                }

                if (id1 == id2 && !wordI.equals(wordJ) &&
                        !wordJ.getBlock().equals(block)) {
                    if (separate(wordI)) {
                        i = -1;
                        j = -1;
                    }

                    if (separate(wordJ)) {
                        i = -1;
                        j = -1;
                    }
                }
            }
        }
    }

    private boolean separate(Word word) {
        Block block = word.getBlock();

        if (block.getWords().size() > 1) {
            List<Word> words = block.getWords();
            int id1 = word.getStartChunkID();

            for (int i = 0; i < words.size(); i++) {
                Word wordI = words.get(i);

                if (id1 == wordI.getStartChunkID()) {
                    block.removeWord(wordI);
                    i--;

                    this.blocks.add(new Block(wordI));
                    this.words.remove(wordI);

                    if (block.getWords().isEmpty()) {
                        blocks.remove(block);
                        break;
                    }
                }
            }
            return true;
        }

        return false;
    }
}