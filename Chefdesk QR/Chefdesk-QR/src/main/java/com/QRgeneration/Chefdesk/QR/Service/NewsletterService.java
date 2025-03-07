package com.QRgeneration.Chefdesk.QR.Service;

import com.QRgeneration.Chefdesk.QR.Repository.NewsletterRepository;
import com.QRgeneration.Chefdesk.QR.models.Newsletter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class NewsletterService {

    private final NewsletterRepository newsletterRepository;

    public NewsletterService(NewsletterRepository newsletterRepository) {
        this.newsletterRepository = newsletterRepository;
    }

    public String checkForEqualMails(MultipartFile file) throws IOException {
        List<String[]> fileData = readExcel(file);
        int updatedCount = 0;

        for (int i = 1; i < fileData.size(); i++) {
            String[] row = fileData.get(i);
            String email = getSafeString(getValue(row, 0));

            if (!email.equals("NULL")) {
                Optional<Newsletter> newsletterOpt = newsletterRepository.findByEmail(email);
                System.out.println("CHECKING FOR :: "+ email);
                if (newsletterOpt.isPresent()) {
                    Newsletter newsletter = newsletterOpt.get();
                    if (!Boolean.TRUE.equals(newsletter.getActive())) {
                        newsletter.setActive(true);
                        newsletterRepository.save(newsletter);
                        System.err.println("EMAIL FOUND ::: "+ email);
                        updatedCount++;

                    }
                }
            }
        }
        return "Updated " + updatedCount + " records successfully.";
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

    private String getSafeString(String value) {
        return value == null || value.trim().isEmpty() ? "NULL" : value.trim();
    }

    private String getValue(String[] row, int index) {
        return (index >= 0 && index < row.length) ? row[index] : "NULL";
    }
}
