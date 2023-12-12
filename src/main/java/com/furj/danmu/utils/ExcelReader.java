package com.furj.danmu.utils;

import javafx.scene.text.Text;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ExcelReader {
    public static List<Text> readExcel(String path, int startRow, int count) throws IOException, InvalidFormatException {
        // 打开Excel文件
        File file = new File(path);
        OPCPackage opcPackage = OPCPackage.open(file);

        try (XSSFWorkbook workbook = new XSSFWorkbook(opcPackage)) {
            // 获取工作表
            XSSFSheet sheet = workbook.getSheetAt(0);

            // 使用流读取数据
            Iterable<Row> rows = sheet::rowIterator;
            List<Text> texts = StreamSupport.stream(rows.spliterator(), false)
                    .skip(startRow)
                    .limit(count)
                    .map(row -> {
                        Cell wordCell = row.getCell(0);
                        String word = wordCell.toString();

                        Cell definitionCell = row.getCell(1);
                        String definition = definitionCell.toString().replaceAll("\\r?\\n", " ");

                        String textString = word + "  " + definition;
                        return new Text(textString);
                    })
                    .collect(Collectors.toList());

            return texts;
        } finally {
            // 关闭输入流
            opcPackage.close();
        }
    }

}
