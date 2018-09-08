package ru.icc.td.tabbypdf2.comp;

import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Word;
import ru.icc.td.tabbypdf2.model.Block;

import java.util.List;

public final class BlockComposer {
    public void composeBlocks(Page page) {
        List<Word> words = page.getWords();

        if (words.isEmpty())
            return;

        List<Block> blocks = composeBlocks(words);
        page.addWords(words);
    }

    private List<Block> composeBlocks(List<Word> words) {

        return null;
    }
}
