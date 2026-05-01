package com.ai_question_paper_generator.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class OcrService {

    // New method — accepts MultipartFile directly, no disk I/O
    public String extractText(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return extractTextFromStream(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file for OCR", e);
        }
    }

    // Core logic — reusable with any InputStream
    private String extractTextFromStream(InputStream inputStream) {
        ITesseract tesseract = buildTesseract();
        StringBuilder finalText = new StringBuilder();

        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFRenderer renderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage originalImage = renderer.renderImageWithDPI(page, 400);
                BufferedImage grayImage = toGrayscale(originalImage);

                String pageText = tesseract.doOCR(grayImage);
                finalText.append(pageText).append("\n\n");
            }

            return finalText.toString();

        } catch (IOException | TesseractException e) {
            throw new RuntimeException("OCR processing failed", e);
        }
    }

    private ITesseract buildTesseract() {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("eng");
        tesseract.setOcrEngineMode(1);
        tesseract.setVariable("user_defined_dpi", "400");
        return tesseract;
    }

    private BufferedImage toGrayscale(BufferedImage original) {
        BufferedImage grayImage = new BufferedImage(
                original.getWidth(),
                original.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY
        );
        Graphics2D g = grayImage.createGraphics();
        g.drawImage(original, 0, 0, null);
        g.dispose();
        return grayImage;
    }
}
