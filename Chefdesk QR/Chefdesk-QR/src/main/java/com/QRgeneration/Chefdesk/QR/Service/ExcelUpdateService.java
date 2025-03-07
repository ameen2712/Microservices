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
public class ExcelUpdateService {

    public List<String> processExcelFiles(MultipartFile file1, MultipartFile file2, String screenId) throws IOException {
        List<String[]> file1Data = readExcel(file1);
        List<String[]> file2Data = readExcel(file2);
        List<String> updateQueries = new ArrayList<>();

        int minRows = Math.min(file1Data.size(), file2Data.size());
        for (int i = 1; i < minRows; i++) {
            String[] row1 = file1Data.get(i);
            String[] row2 = file2Data.get(i);

            String screenRefId1 = parseNumber(getValue(row1, 1));
            String rowNo1 = parseString(getValue(row1, 2));
            String colNo1 = parseString(getValue(row1, 3));
            String rowName1 = parseNullableString(getValue(row1, 4));
            String colName1 = parseNullableString(getValue(row1, 5));
            String seatFlag1 = parseNumber(getValue(row1, 6));
            String seatId1 = parseNullableString(getValue(row1, 7));
            String image1 = parseString(getValue(row1, 9));
            String tier1 = parseNullableString(getValue(row1, 10));
            String id2 = parseNumber(getValue(row2, 0));
            String internetBooked = parseNumber(getValue(row1, 11));
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
        return "'" + (value.endsWith(".0") ? value.substring(0, value.length() - 2) : value.trim()) + "'";
    }

    private String getValue(String[] row, int index) {
        return index < row.length ? row[index] : "NULL";
    }




    public List<String> processExcelFiles2(MultipartFile file1, MultipartFile file2) throws IOException {
        List<String[]> file1Data = readExcel(file1);
        List<String[]> file2Data = readExcel(file2);
        List<String> updateQueries = new ArrayList<>();

        for (int i = 1; i < file1Data.size(); i++) {
            String[] row1 = file1Data.get(i);

            // Ensure non-null and properly trimmed values
            String seatId1 = getSafeString(getValue(row1, 7)); // SEAT_ID from file1
            String rowNo1 = formatNumber(getSafeString(getValue(row1, 2))); // ROW_NO from file1
//            System.out.println("SEAT ID 1: '" + seatId1 + "'"); // Debugging

            // Skip invalid seatId1
            if (seatId1.isEmpty()) {
                continue;
            }

            for (int j = 1; j < file2Data.size(); j++) {
                String[] row2 = file2Data.get(j);

                // Ensure non-null and properly trimmed values
                String seatId2 = getSafeString(getValue(row2, 11)); // SEAT_ID from file2
                String id2 = formatNumber(getSafeString(getValue(row2, 0))); // ID from file2
//                System.out.println("SEAT ID 2: '" + seatId2 + "'"); // Debugging

                // Skip invalid seatId2
                if (seatId2.isEmpty()) {
                    continue;
                }

                if (seatId1.equalsIgnoreCase(seatId2)) {
                    String query = String.format(
                            "UPDATE cineroyaldb.screen_seat_mgmt SET xtertain_row_no='%s' WHERE id=%s;",
                            rowNo1, id2
                    );
                    System.err.println(query);
                    updateQueries.add(query);
                    break; // Stop searching once a match is found
                }
            }
        }

        return updateQueries;
    }

    /**
     * Helper method to safely convert values to non-null strings.
     */
    private String getSafeString(Object value) {
        return (value == null || "NULL".equalsIgnoreCase(value.toString().trim())) ? "" : value.toString().trim();
    }

    /**
     * Helper method to remove `.0` from numbers when they represent whole numbers.
     */
    private String formatNumber(String value) {
        if (value.matches("\\d+\\.0+")) { // Matches numbers ending in .0
            return value.substring(0, value.indexOf('.')); // Remove the .0
        }
        return value;
    }

    public List<String> processExcelFiles3(MultipartFile file1, MultipartFile file2) throws IOException {
        List<String[]> file1Data = readExcel(file1);
        List<String[]> file2Data = readExcel(file2);
        List<String> updateQueries = new ArrayList<>();

        // Ensure both files have the same number of rows before proceeding
        if (file1Data.size() != file2Data.size()) {
            throw new IllegalArgumentException("Files do not have the same number of rows.");
        }

        // Iterate through each row (starting from index 1, assuming index 0 is the header)
        for (int i = 1; i < file1Data.size(); i++) {
            String[] rowFile1 = file1Data.get(i);
            String[] rowFile2 = file2Data.get(i);

            // Extract values
            String rowNo1 = formatNumber(getSafeString(getValue(rowFile1, 2))); // ROW_NO from file1
            String id2 = formatNumber(getSafeString(getValue(rowFile2, 0))); // ID from file2

            // Ensure non-empty values before creating query
            if (!rowNo1.isEmpty() && !id2.isEmpty()) {
                String query = String.format(
                        "UPDATE cineroyaldb.screen_seat_mgmt SET xtertain_row_no='%s' WHERE id=%s;",
                        rowNo1, id2
                );
                System.err.println(query);
                updateQueries.add(query);
            }
        }

        return updateQueries;
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
