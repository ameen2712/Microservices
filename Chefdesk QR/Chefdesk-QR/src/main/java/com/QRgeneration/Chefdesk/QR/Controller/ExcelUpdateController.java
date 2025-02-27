package com.QRgeneration.Chefdesk.QR.Controller;

import com.QRgeneration.Chefdesk.QR.Service.ExcelUpdateService;
import com.QRgeneration.Chefdesk.QR.Service.ExcelUpdateService2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelUpdateController {

    private final ExcelUpdateService excelUpdateService;

    private final ExcelUpdateService2 excelUpdateService2;

    public ExcelUpdateController(ExcelUpdateService excelUpdateService, ExcelUpdateService2 excelUpdateService2) {
        this.excelUpdateService = excelUpdateService;
        this.excelUpdateService2 = excelUpdateService2;
    }

    @PostMapping("/processForEqual")
    public ResponseEntity<List<String>> processExcelFiles(@RequestParam("file1") MultipartFile file1,
                                                          @RequestParam("file2") MultipartFile file2,
                                                          @RequestParam("screenId") String screenId) {
        try {
            List<String> queries = excelUpdateService.processExcelFiles(file1, file2, screenId);
            return ResponseEntity.ok(queries);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(List.of("Error processing files: " + e.getMessage()));
        }
    }

    @PostMapping("/processForEqual2")
    public ResponseEntity<List<String>> processExcelFiles3(@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2)
                                                          {
        try {
            List<String> queries = excelUpdateService.processExcelFiles2(file1, file2);
            return ResponseEntity.ok(queries);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(List.of("Error processing files: " + e.getMessage()));
        }
    }

    @PostMapping("/processForNotEqual")
    public ResponseEntity<List<String>> processExcelFiles2(@RequestParam("file1") MultipartFile file1,
                                                          @RequestParam("file2") MultipartFile file2,
                                                          @RequestParam("screenId") String screenId) {
        try {
            List<String> queries = excelUpdateService2.processExcelFiles(file1, file2, screenId);
            return ResponseEntity.ok(queries);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(List.of("Error processing files: " + e.getMessage()));
        }
    }

}