package ru.icc.td.tabbypdf2.comp;

import ru.icc.td.tabbypdf2.model.Page;

public class PageComposer {
    private final WordComposer wordComposer = new WordComposer();
    private final BlockComposer blockComposer = new BlockComposer();
    private final InterColumnGapExtractor interColumnGapExtractor = new InterColumnGapExtractor();
    private final LineComposer lineComposer = new LineComposer();

    public void compose(Page page) {
        if (null == page)
            return;

        wordComposer.composeWords(page);
        lineComposer.composeLines(page);
        blockComposer.composeBlocks(page);
        interColumnGapExtractor.composeGaps(page);
    }
}
