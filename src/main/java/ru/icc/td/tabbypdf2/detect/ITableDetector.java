package ru.icc.td.tabbypdf2.detect;

import org.apache.pdfbox.pdmodel.PDPage;
import ru.icc.td.tabbypdf2.model.Page;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.List;

public interface ITableDetector {

    List<Rectangle2D> detectTables(Page page) throws IOException;

}
