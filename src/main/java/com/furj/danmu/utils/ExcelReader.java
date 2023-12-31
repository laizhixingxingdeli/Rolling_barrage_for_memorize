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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ExcelReader {
    public static List<Text> readExcel(String path, int startRow, int count) throws IOException, InvalidFormatException {
        // 打开Excel文件
        File file = new File(path);

        try (OPCPackage opcPackage = OPCPackage.open(file); XSSFWorkbook workbook = new XSSFWorkbook(opcPackage)) {
            // 获取工作表
            XSSFSheet sheet = workbook.getSheetAt(0);

            // 检查实际行数
            int rowCount = sheet.getLastRowNum() + 1;
            if (startRow >= rowCount) {
                // 数据行数不足
                return Collections.emptyList();
            }

            // 计算实际可用的行数
            int availableRowCount = Math.min(count, rowCount - startRow);

            // 使用流读取数据
            Iterable<Row> rows = sheet::rowIterator;

            return StreamSupport.stream(rows.spliterator(), false)
                    .skip(startRow)
                    .limit(availableRowCount)
                    .map(row -> {
                        Cell wordCell = row.getCell(0);
                        String word = wordCell.toString();

                        Cell definitionCell = row.getCell(1);
                        String definition = definitionCell.toString().replaceAll("\\r?\\n", " ");

                        String textString = word + "  " + definition;
                        return new Text(textString);
                    })
                    .collect(Collectors.toList());
        }
        // 关闭输入流
    }
}
