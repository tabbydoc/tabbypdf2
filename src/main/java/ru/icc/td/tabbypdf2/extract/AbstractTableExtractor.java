package ru.icc.td.tabbypdf2.extract;

import ru.icc.td.tabbypdf2.model.Page;
//import ru.icc.td.tabbypdf2.model.TableArea;

import java.util.List;

public abstract class AbstractTableExtractor {

    private Page page = null;

    protected AbstractTableExtractor(Page page){
        this.page = page;
    }

    protected Page getPage() {
        return page;
    }

    protected abstract List<TableArea> extract();

}
