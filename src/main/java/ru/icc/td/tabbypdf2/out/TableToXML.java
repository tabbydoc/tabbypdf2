package ru.icc.td.tabbypdf2.out;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.icc.td.tabbypdf2.model.Table;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.List;

public class TableToXML extends Writer {

    private String fileName;
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = null;
    Document doc = null;

    public TableToXML(List<Table> tables, String fileName) {
        super(tables, fileName);
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException();
        }

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("document");
        rootElement.setAttribute("filename", fileName);
        doc.appendChild(rootElement);
    }

    @Override
    void writeHeader(String fileName) {
        Element tableElement = doc.createElement("table");
        //tableElement.setAttribute("id", String.valueOf(i + 1));

        Element regionElement = doc.createElement("region");
        regionElement.setAttribute("col-increment", "0");
        regionElement.setAttribute("row-increment", "0");
        regionElement.setAttribute("id", "1");
        //regionElement.setAttribute("page", String.valueOf(table.getPageNumber() + 1));
    }

    @Override
    void writeFooter() {

    }

    @Override
    void writeTable(Table table, int i) {

    }
}
