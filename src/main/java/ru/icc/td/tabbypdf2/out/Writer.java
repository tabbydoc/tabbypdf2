package ru.icc.td.tabbypdf2.out;

import ru.icc.td.tabbypdf2.model.Table;

import java.util.ArrayList;
import java.util.List;

public abstract class Writer {

    private List<String> out;
    private List<Table> tables;
    private String fileName;

    public void Writer(List<Table> tables, String fileName) {
        out = new ArrayList<String>();
        this.tables = tables;
        this.fileName = fileName;
    }

    public List<String> write() {
        writeHeader(fileName);
        writeBody();
        writeFooter();
        return out;
    }

    abstract void writeHeader(String fileName);

    private void writeBody() {
        for (int i = 0; i <  tables.size(); i++) {
            Table table = tables.get(i);
            writeTable(table, i);
        }
    }

    protected void writeString(String s) {
        out.add(s);
    }

    abstract void writeFooter();

    abstract void writeTable(Table table, int i);
}
