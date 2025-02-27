package com.QRgeneration.Chefdesk.QR.Controller;

import com.QRgeneration.Chefdesk.QR.Service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.zip.*;

@RestController
@RequestMapping("/api/qr")
public class QrCodeController {

    @Autowired
    private QrCodeService qrCodeService;
// using InputStreamResource to directly download the file

    @PostMapping("/generate")
    public ResponseEntity<InputStreamResource> generateQRCodes(@RequestBody List<String> roomNumbers) throws IOException {
        for (String roomNumber : roomNumbers) {
            try {
                qrCodeService.generateQrCode(roomNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String zipFileName = "qr_codes.zip";
        Path zipFilePath = Paths.get(zipFileName);

        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
            System.out.println("try block");;
            for (String roomNumber : roomNumbers) {
                Path qrFilePath = Paths.get("C:\\Users\\ameen\\OneDrive\\Desktop\\generated_qrs\\" + roomNumber + ".png");
                if (Files.exists(qrFilePath)) {
                    try (InputStream qrInputStream = Files.newInputStream(qrFilePath)) {
                        ZipEntry zipEntry = new ZipEntry(roomNumber + ".png");
                        zipOut.putNextEntry(zipEntry);
                        qrInputStream.transferTo(zipOut);
                        zipOut.closeEntry();
                    }
                }
            }
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFilePath.toFile()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
