package ru.icc.td.tabbypdf2.detect;

import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Document;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Table;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class PostProcessing {
    //TODO:
    // 1. Верификация
    //      - Абзацы
    //      - Изображения
    //      - Пустые области
    // 2. Конструктор пуст? Вызывается метод для обработки вида public Rectangle2D process(Rectangle2D bounding)

    public PostProcessing() {

    }

    private Page page;
    private List<Table> buffer = new ArrayList<>();

    public void process(Document document) {
        if (null == document)
            return;

        List<Page> pages = document.getPages();

        if (pages.isEmpty())
            return;

        for (Page page : pages) {
            List<Table> tables = page.getTables();


            if(tables.isEmpty())
                continue;

            this.page = page;

            for(Table table : tables){
                processTable(table);
            }

            page.getTables().removeAll(buffer);
        }
    }

    private void processTable(Table table){
        buffer.clear();

        //Rule 1: images
        for(Rectangle2D rectangle2D : page.getImageBounds()) {
            if (table.intersects(rectangle2D) || table.getBlocks().isEmpty())
                buffer.add(table);
        }

        //Rule 2: paragraphs



    }

}
