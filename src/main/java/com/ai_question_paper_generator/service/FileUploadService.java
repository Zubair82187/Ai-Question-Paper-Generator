package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.book_dto.BookDtoBasic;
import com.ai_question_paper_generator.dto.book_dto.BookDtoWithId;
import com.ai_question_paper_generator.dto.book_dto.BookDtoWithPath;
import com.ai_question_paper_generator.exception.FileNotAllowedException;
import com.ai_question_paper_generator.exception.FileNotSavedException;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;


@AllArgsConstructor
@Service
public class FileUploadService {
    private final BookService bookService;
    private final Path root = Paths.get("uploads");
    private final OcrService ocrService;
    private final ChunkService chunkService;

    // Save file
    @Transactional
    public String saveFile(MultipartFile file, BookDtoBasic book){
        Path destination = saveFileIntoFileSytem(file);
        BookDtoWithId bookDtoWithId = bookService.saveBook(
                new BookDtoWithPath(book.getBookName(), book.getSubject(), destination.toString())
        );

        chunkBook(destination, bookDtoWithId);
        return "file uploaded successfully";
    }

    // Save the file into file system
    private Path saveFileIntoFileSytem(MultipartFile file){

        if (file == null || file.isEmpty()) {
            throw new FileNotAllowedException("File is empty or no file");
        }

        String originalName = Optional.ofNullable(file.getOriginalFilename())
                .orElse("unknown.pdf");
        originalName = Paths.get(originalName).getFileName().toString();

        Path destination = null;

        if (!originalName.toLowerCase().endsWith(".pdf")) {
            throw new FileNotAllowedException("Only PDF allowed");
        }

        try {

            // Ensure directory exists
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            String fileName = UUID.randomUUID() + "_" + originalName;
            destination = root.resolve(fileName);

            // Save file in file system
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }
            return destination;

        } catch (Exception e) {

            // delete file if DB fails
            if (destination != null) {
                try {
                    boolean deleted = Files.deleteIfExists(destination);

                    if (!deleted) {
                        System.out.println("File not found for deletion: " + destination);
                    }

                } catch (IOException ioException) {
                    throw new RuntimeException(
                            "Failed to delete file: " + destination, ioException
                    );
                }
            }

            throw  new FileNotSavedException("File upload failed : "+ e);
        }

    }


    // Extract text from the digital file.
    private String extractTextFromFile(String filePath){

        Path path = Path.of(filePath);

        if(!Files.exists(path)){
            throw new IllegalArgumentException("file not found");
        }

        try(InputStream inputStream = Files.newInputStream(path);
            PDDocument document = PDDocument.load(inputStream)){
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Extract text from the scanned file
    private String extractTextFromScannedFile(String filePath) {
        return ocrService.extractText(filePath);
    }

    // Check the file is scanned or not
    private boolean isScannedPdf(InputStream inputStream){

        try(PDDocument document = PDDocument.load(inputStream)){
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            return text == null || text.trim().isEmpty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Chunk the book
    private void chunkBook(Path path, BookDtoWithId bookDtoWithId){
        String text = extractText(path)
                .replaceAll("[^\\x00-\\x7F]", " ")
                .replaceAll("\\s+", " ")
                .trim();
        chunkService.chunkBook(text, bookDtoWithId);
    }

    private String extractText(Path path){
        String text;

        try {
            if(isScannedPdf(Files.newInputStream(path))){
                text = extractTextFromScannedFile(path.toString());
            }
            else {
                text = extractTextFromFile(path.toString());
            }
            return text;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}