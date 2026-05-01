package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.book_dto.BookDtoBasic;
import com.ai_question_paper_generator.dto.book_dto.BookDtoWithId;
import com.ai_question_paper_generator.exception.FileNotAllowedException;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@AllArgsConstructor
@Service
public class FileUploadService {
    private final BookService bookService;
    private final OcrService ocrService;
    private final ChunkService chunkService;

    @Transactional
    public String saveFile(MultipartFile file, BookDtoBasic book) {
        validateFile(file);

        String text = extractText(file);
        String cleanedText = text
                .replaceAll("[^\\x00-\\x7F]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        // Save book metadata
        BookDtoWithId bookDtoWithId = bookService.saveBook(
                new BookDtoBasic(book.getBookName(), book.getSubject())
        );

        chunkService.chunkBook(cleanedText, bookDtoWithId);
        return "File processed and chunked successfully";
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileNotAllowedException("File is empty or missing");
        }

        String originalName = Optional.ofNullable(file.getOriginalFilename())
                .orElse("");

        if (!originalName.toLowerCase().endsWith(".pdf")) {
            throw new FileNotAllowedException("Only PDF files are allowed");
        }
    }

    private String extractText(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            if (isScannedPdf(file)) {
                // OCR path — still needs a temp approach (see note below)
                return ocrService.extractText(file);
            } else {
                return extractTextFromStream(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + e.getMessage(), e);
        }
    }

    private String extractTextFromStream(InputStream inputStream) {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract text from PDF", e);
        }
    }

    private boolean isScannedPdf(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return text == null || text.trim().isEmpty();
        } catch (IOException e) {
            throw new RuntimeException("Failed to inspect PDF", e);
        }
    }
}