package ru.icc.td.tabbypdf2.model;

import java.awt.geom.Rectangle2D;

public class Table extends Rectangle2D.Float {

    private int page;

    public Table(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
