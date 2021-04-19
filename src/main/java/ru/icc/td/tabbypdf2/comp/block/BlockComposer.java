package ru.icc.td.tabbypdf2.comp.block;

import ru.icc.td.tabbypdf2.comp.block.refinement.BlockProcessing;
import ru.icc.td.tabbypdf2.interfaces.Algorithm;
import ru.icc.td.tabbypdf2.interfaces.Composer;
import ru.icc.td.tabbypdf2.interfaces.Processing;
import ru.icc.td.tabbypdf2.model.Page;

public final class BlockComposer implements Composer<Page> {
    private final Algorithm<Page> algorithm = new TRecsAlgorithm();
    private final Processing<Page> processing = new BlockProcessing();

    public BlockComposer() {
    }

    @Override
    public void compose(Page page) {
        // Initial algorithm
        algorithm.start(page);
        // Rough block processing
        processing.process(page);
    }
}