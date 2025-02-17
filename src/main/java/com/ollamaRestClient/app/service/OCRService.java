package com.ollamaRestClient.app.service;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class OCRService {

    public String extractTextFromScannedPdf(MultipartFile file) throws IOException, TesseractException {
        // Load the PDF document
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        // Initialize Tesseract
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata"); // Set the path to your tessdata directory
        tesseract.setLanguage("eng"); // Set the language

        // Extract text from each page
        StringBuilder extractedText = new StringBuilder();
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300); // Render image at 300 DPI
            String text = tesseract.doOCR(image); // Perform OCR on the image
            extractedText.append(text).append("\n");
        }

        // Close the document
        document.close();
        return extractedText.toString();
    }
}