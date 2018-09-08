package ru.icc.td.tabbypdf2.comp;

import ru.icc.td.tabbypdf2.model.Page;

public class PageComposer {
    private final WordComposer wordComposer = new WordComposer();

    public void compose(Page page) {
        if (null == page)
            return;

        wordComposer.composeWords(page);
    }
}
