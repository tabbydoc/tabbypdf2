package ru.icc.td.tabbypdf2.out;

import ru.icc.td.tabbypdf2.model.Table;

import java.util.List;

public class XmlWriter extends Writer {

    private static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static String XML_SCHEMA = "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"competition-entry-region-model.xsd\"";


    public XmlWriter(List<Table> tables, String filename) {
        super(tables, filename);
    }

    @Override
    void writeHeader(String fileName) {
        writeString(XML_HEADER);
        writeString(String.format("<document filename=\"%s\" \n %s>", fileName, XML_SCHEMA));
    }

    @Override
    void writeFooter() {
        writeString("</document>");
    }

    @Override
    void writeTable(Table table, int tableId) {
        writeString(String.format("<table id='%s'>", String.valueOf(tableId)));
        writeString(String.format("<region id='0' page='%s'>", table.getPage()));
        int x1 = (int) table.getMinX();
        int y1 = (int) table.getMinY();
        int x2 = (int) table.getMaxX();
        int y2 = (int) table.getMaxY();
        writeString(String.format("<bounding-box x1='%d' y1='%d' x2='%d' y2='%d'/>", x1, y1, x2, y2));
    }
}
