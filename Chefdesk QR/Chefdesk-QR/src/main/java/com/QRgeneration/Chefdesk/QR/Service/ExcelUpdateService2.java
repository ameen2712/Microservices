package com.QRgeneration.Chefdesk.QR.Service;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelUpdateService2 {

    public List<String> processExcelFiles(MultipartFile file1, MultipartFile file2, String screenId) throws IOException {
        List<String[]> file1Data = readExcel(file1);
        List<String[]> file2Data = readExcel(file2);
        List<String> updateQueries = new ArrayList<>();

        int maxRows = file1Data.size();

        for (int i = 1; i < maxRows; i++) {
            String[] row1 = file1Data.get(i);
            String[] row2 = file2Data.get(i);

            String screenRefId1 = parseNumber(getValue(row1, 1));
            String rowNo1 = parseString(getValue(row1, 2));
            String colNo1 = parseString(getValue(row1, 3));
            String rowName1 = parseNullableString(getValue(row1, 4));
            String colName1 = parseString(getValue(row1, 5));
            String seatFlag1 = parseNumber(getValue(row1, 6));
            String seatId1 = parseNullableString(getValue(row1, 7));
            String image1 = parseString(getValue(row1, 9));
            String tier1 = parseNullableString(getValue(row1, 10));
            String id2 = parseNumber(getValue(row2, 0));
            String internetBooked = parseNumber(getValue(row1,11));
//            System.out.println("internet booked  >>" + internetBooked);

            String query = String.format(
                    "UPDATE cineroyaldb.screen_seat_mgmt SET " +
                            "version=0, screen_id=%s, internet_booked=%s, image=%s, tier=%s, seat_flag=%s, " +
                            "row_no=%s, col_name=%s, col_no=%s, screen_ref_id=%s, seat_id=%s, row_name=%s " +
                            "WHERE id=%s;",
                    parseNumber(screenId),
                    parseNumber(internetBooked),
                    image1,
                    tier1,
                    seatFlag1,
                    rowNo1,
                    colName1,
                    colNo1,
                    screenRefId1,
                    seatId1,
                    rowName1,
                    id2
            );

            System.err.println(query);
            updateQueries.add(query);
        }

        return updateQueries;
    }

    private String parseNumber(String value) {
        if (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("NULL") || value.equalsIgnoreCase("-1.0")) {
            return "NULL";
        }
        try {
            double num = Double.parseDouble(value);
            return (num == Math.floor(num)) ? String.valueOf((int) num) : String.valueOf(num);
        } catch (NumberFormatException e) {
            return "NULL";
        }
    }

    private String parseString(String value) {
        if (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("null") || value.equals("-1.0")) {
            return "''";
        }
        return "'" + (value.endsWith(".0") ? value.substring(0, value.length() - 2) : value.trim()) + "'";
    }


    private String parseNullableString(String value) {
        if (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("NULL") || value.equalsIgnoreCase("-1.0")) {
            return "NULL";
        }
        return "'" + value.trim() + "'";
    }

    private String getValue(String[] row, int index) {
        return index < row.length ? row[index] : "NULL";
    }

    private List<String[]> readExcel(MultipartFile file) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                List<String> rowData = new ArrayList<>();
                int lastColumn = row.getLastCellNum();
                for (int i = 0; i < lastColumn; i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.add(cell.toString().trim().isEmpty() ? "NULL" : cell.toString().trim());
                }
                data.add(rowData.toArray(new String[0]));
            }
        }
        return data;
    }
}

