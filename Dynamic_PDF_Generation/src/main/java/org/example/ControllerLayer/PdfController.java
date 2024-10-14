package org.example.ControllerLayer;

import org.example.Dtos.InvoiceRequest;
import org.example.ServiceLayer.PdfGenerator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private static final String PDF_STORAGE_PATH = "/tmp/pdf-storage/";
    private static Map<String, String> storedPdfMap = new HashMap<>();

    // Endpoint to generate PDF
    @PostMapping("/generate")
    public ResponseEntity<?> generatePdf(@RequestBody InvoiceRequest request) throws Exception {
        // Generate a hash for the request data
        String requestHash = generateHash(request);

        // Check if PDF is already stored for this data
        if (storedPdfMap.containsKey(requestHash)) {
            String filePath = storedPdfMap.get(requestHash);
            return ResponseEntity.ok().body("PDF already generated. You can download it from: /api/pdf/download/" + requestHash);
        }

        // Generate PDF using iText
        byte[] pdfData = PdfGenerator.generatePdf(request);

        // Store the PDF
        String fileName = requestHash + ".pdf";
        storePdf(pdfData, fileName);

        // Save the hash to file mapping
        storedPdfMap.put(requestHash, PDF_STORAGE_PATH + fileName);

        return ResponseEntity.ok().body("PDF generated. You can download it from: /api/pdf/download/" + requestHash);
    }

    // Endpoint to download PDF
    @GetMapping("/download/{hash}")
    public ResponseEntity<?> downloadPdf(@PathVariable String hash) throws IOException {
        String filePath = storedPdfMap.get(hash);
        if (filePath == null) {
            return ResponseEntity.status(404).body("PDF not found for the given hash.");
        }

        File file = new File(filePath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    // Utility method to store PDF locally
    private void storePdf(byte[] pdfData, String fileName) throws IOException {
        Files.createDirectories(Paths.get(PDF_STORAGE_PATH));
        Files.write(Paths.get(PDF_STORAGE_PATH + fileName), pdfData);
    }

    // Generate hash from the request data to avoid regeneration
    private String generateHash(InvoiceRequest request) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(request.toString().getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}