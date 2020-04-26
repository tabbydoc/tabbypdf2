package ru.icc.td.tabbypdf2.out;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Document;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import static ru.icc.td.tabbypdf2.util.Utils.createOutputFile;

public class ExcelWriter {
    private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet sheet;
    private int rowCount = 0;
    private Path path;

    private int columnCount;
    private Row row;

    public ExcelWriter(Path path) {
        sheet = workbook.createSheet();
        this.path = path;
    }

    public void writeExcel(Document document) {
        for (Page page : document.getPages()) {
            for (Table table : page.getTables()) {
                row = sheet.createRow(rowCount++);
                row.createCell(0).setCellValue(rowCount);
                row.createCell(1).setCellValue(FilenameUtils.getBaseName(document.getFileName()));
                row.createCell(2).setCellValue(page.getIndex() + 1);
                row.createCell(3).setCellValue(table.getMaxY());
                row.createCell(4).setCellValue(table.getBlocks().size());

                Graph<Block, DefaultWeightedEdge> structure = table.getStructure();
                DescriptiveStatistics ds = new DescriptiveStatistics();

                for (DefaultWeightedEdge edge : structure.edgeSet()) {
                    ds.addValue(structure.getEdgeWeight(edge));
                }

                columnCount = 4;
                writeStats(ds);

                ds.clear();
                DescriptiveStatistics dsX = new DescriptiveStatistics();
                DescriptiveStatistics dsY = new DescriptiveStatistics();
                double sum = 0;
                for (Block block : table.getBlocks()) {
                    dsX.addValue(block.getCenterX());
                    dsY.addValue(block.getCenterY());
                    ds.addValue(structure.degreeOf(block));
                    sum = sum + square(block);
                }

                double relation = sum / (table.width * table.height);
                columnCount++;
                row.createCell(columnCount).setCellValue(relation);

                writeStats(dsX);
                columnCount++;
                row.createCell(columnCount).setCellValue(table.getCenterX());

                writeStats(dsY);
                columnCount++;
                row.createCell(columnCount).setCellValue(table.getCenterY());

                writeStats(ds);

                KosarajuStrongConnectivityInspector<Block, DefaultWeightedEdge> inspector =
                        new KosarajuStrongConnectivityInspector<>(structure);

                columnCount++;
                row.createCell(columnCount).setCellValue(inspector.stronglyConnectedSets().size());
            }
        }
    }

    private void writeStats(DescriptiveStatistics ds) {
        row.createCell(columnCount + 1).setCellValue(ds.getSum());
        row.createCell(columnCount + 2).setCellValue(ds.getN());
        row.createCell(columnCount + 3).setCellValue(ds.getMean());
        row.createCell(columnCount + 4).setCellValue(ds.getMax());
        row.createCell(columnCount + 5).setCellValue(ds.getMin());
        row.createCell(columnCount + 6).setCellValue(ds.getPercentile(50));
        row.createCell(columnCount + 7).setCellValue(ds.getStandardDeviation());
        row.createCell(columnCount + 8).setCellValue(ds.getVariance());
        columnCount = columnCount + 8;
    }

    private double square(Block block) {
        return block.width * block.height;
    }

    public void save() throws IOException {
        File file = createOutputFile("excel", path, "book", "xlsx");
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}
