package com.QRgeneration.Chefdesk.QR.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class QrCodeService {

    private static final String BASE_URL = "https://chefdesk.zaravya.com/menus/Ayush_Caterers?roomNo=";
    private static final String OUTPUT_DIRECTORY = "C:\\Users\\ameen\\OneDrive\\Desktop\\generated_qrs\\";

    public void generateQrCode(String roomNumber) throws WriterException, IOException {
        String url = BASE_URL + roomNumber;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 1148, 1148);

        Path outputDirPath = Paths.get(OUTPUT_DIRECTORY);
        if (Files.notExists(outputDirPath)) {
            Files.createDirectories(outputDirPath);
        }
       
        String filePath = OUTPUT_DIRECTORY + roomNumber + ".png";
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
