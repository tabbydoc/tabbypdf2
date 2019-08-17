package ru.icc.td.tabbypdf2.comp.line;

import ru.icc.td.tabbypdf2.interfaces.Algorithm;
import ru.icc.td.tabbypdf2.interfaces.Composer;
import ru.icc.td.tabbypdf2.model.Page;

public class LineComposer implements Composer<Page> {
    private final Algorithm algorithm = new LineAlgorithm();
    private final Algorithm algorithmLC = new LineConstantAlgorithm();

    @Override
    public void compose(Page page) {
        algorithm.start(page);

        algorithmLC.start(page);
    }
}