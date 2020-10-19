package ru.icc.td.tabbypdf2.out;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.icc.td.tabbypdf2.detect.processing.verification.feature.ParameterFactory;
import ru.icc.td.tabbypdf2.model.Document;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import static ru.icc.td.tabbypdf2.util.Utils.createOutputFile;

public class ExcelWriter {
    private final XSSFWorkbook workbook = new XSSFWorkbook();
    private final XSSFSheet sheet;
    private final Path path;
    private int rowCount = 0;

    public ExcelWriter(Path path) {
        sheet = workbook.createSheet();
        this.path = path;
    }

    public void writeExcel(Document document) {

        for (Page page : document.getPages()) {
            for (Table table : page.getTables()) {
                Row row = sheet.createRow(rowCount++);

                ParameterFactory factory = new ParameterFactory(table);

                if (rowCount == 1) {
                    String[] names = factory.getNames();
                    row.createCell(0).setCellValue("#");
                    row.createCell(1).setCellValue("Name");
                    row.createCell(2).setCellValue("Page");

                    for (int i = 0; i < factory.size; i++) {
                        row.createCell(i + 3).setCellValue(names[i]);
                    }

                    row = sheet.createRow(rowCount++);
                }

                row.createCell(0).setCellValue(rowCount - 1);
                row.createCell(1).setCellValue(FilenameUtils.getBaseName(document.getFileName()));
                row.createCell(2).setCellValue(page.getIndex() + 1);

                double[] parameters = factory.getParameters();

                for (int i = 0; i < factory.size; i++) {
                    row.createCell(i + 3).setCellValue(parameters[i]);
                }
            }
        }
    }

    public void save() throws IOException {
        File file = createOutputFile("excel", path, "book", "xlsx");
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
