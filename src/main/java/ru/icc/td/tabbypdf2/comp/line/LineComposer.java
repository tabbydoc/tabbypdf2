package ru.icc.td.tabbypdf2.comp.line;

import ru.icc.td.tabbypdf2.interfaces.Algorithm;
import ru.icc.td.tabbypdf2.interfaces.Composer;
import ru.icc.td.tabbypdf2.model.Page;

public final class LineComposer implements Composer<Page> {
    private final Algorithm<Page> algorithm = new LineAlgorithm();
    private final Algorithm<Page> algorithmLC = new LineSpacingAlgorithm();

    @Override
    public void compose(Page page) {
        algorithm.start(page);

        algorithmLC.start(page);
    }
}