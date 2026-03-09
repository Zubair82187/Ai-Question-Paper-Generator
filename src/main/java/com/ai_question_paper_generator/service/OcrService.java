package com.ai_question_paper_generator.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class OcrService {

    public String extractText(String path){
        ITesseract tesseract = new Tesseract();
        StringBuilder finalText = new StringBuilder();

        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("eng");
        tesseract.setOcrEngineMode(1);

        // Tell Tesseract actual DPI
        tesseract.setVariable("user_defined_dpi", "400");
        try (PDDocument document = PDDocument.load(new File(path))){

            PDFRenderer renderer = new PDFRenderer(document);

            for(int page=0; page<document.getNumberOfPages(); page++){
                BufferedImage originalImage = renderer.renderImageWithDPI(page, 400);

                // Only grayscale
                BufferedImage grayImage = new BufferedImage(
                        originalImage.getWidth(),
                        originalImage.getHeight(),
                        BufferedImage.TYPE_BYTE_GRAY
                );

                Graphics2D g = grayImage.createGraphics();
                g.drawImage(originalImage, 0, 0, null);
                g.dispose();

                String pageText = tesseract.doOCR(grayImage);

                finalText.append(pageText).append("\n\n");
            }

            return finalText.toString();

        } catch (IOException | TesseractException e) {
            throw new RuntimeException(e);
        }
    }
}
