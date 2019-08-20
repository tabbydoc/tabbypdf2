package ru.icc.td.tabbypdf2.comp;

import ru.icc.td.tabbypdf2.comp.block.BlockComposer;
import ru.icc.td.tabbypdf2.comp.line.LineComposer;
import ru.icc.td.tabbypdf2.model.Page;

public final class PageComposer {
    private final WordComposer wordComposer = new WordComposer();
    private final BlockComposer blockComposer = new BlockComposer();
    private final InterColumnGapExtractor interColumnGapExtractor = new InterColumnGapExtractor();
    private final LineComposer lineComposer = new LineComposer();

    public void compose(Page page) {
        if (null == page)
            return;

        wordComposer.compose(page);
        lineComposer.compose(page);
        blockComposer.compose(page);
        // interColumnGapExtractor.composeGaps(page);
    }
}
