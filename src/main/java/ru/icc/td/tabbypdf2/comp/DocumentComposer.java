package ru.icc.td.tabbypdf2.comp;

import ru.icc.td.tabbypdf2.interfaces.Composer;
import ru.icc.td.tabbypdf2.model.Document;
import ru.icc.td.tabbypdf2.model.Page;

import java.util.List;

public final class DocumentComposer implements Composer<Document> {
    private final PageComposer pageComposer = new PageComposer();

    public void compose(Document document) {
        if (null == document)
            return;

        List<Page> pages = document.getPages();

        if (pages.isEmpty())
            return;

        for (Page page : pages) {
            pageComposer.compose(page);
        }
    }
}
