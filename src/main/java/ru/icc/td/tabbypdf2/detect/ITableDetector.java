package ru.icc.td.tabbypdf2.debug;

import org.apache.pdfbox.pdmodel.PDPage;
import ru.icc.td.tabbypdf2.model.Page;

import java.awt.geom.Rectangle2D;
import java.util.List;

public interface ITableDetector {

    public List<Rectangle2D> detectTables(Page page);
    
}
