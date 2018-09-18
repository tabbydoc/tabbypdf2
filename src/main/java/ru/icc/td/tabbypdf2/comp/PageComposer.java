package ru.icc.td.tabbypdf2.comp;

import ru.icc.td.tabbypdf2.model.Page;

public class PageComposer {
    private final WordComposer wordComposer = new WordComposer();
    private final BlockComposer blockComposer = new BlockComposer();
    private final PageLayoutAlgorithm pageLayoutAlgorithm = new PageLayoutAlgorithm();

    public void compose(Page page) {
        if (null == page)
            return;

        wordComposer.composeWords(page);
        blockComposer.composeBlocks(page);
        pageLayoutAlgorithm.composeGaps(page);
    }
}
