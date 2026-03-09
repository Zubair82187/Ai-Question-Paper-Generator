package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.book_dto.BookDtoBasic;
import com.ai_question_paper_generator.dto.book_dto.BookDtoWithPath;
import com.ai_question_paper_generator.dto.chapter_dto.ChapterDto;
import com.ai_question_paper_generator.exception.FileNotAllowedException;
import com.ai_question_paper_generator.exception.FileNotSavedException;
import com.ai_question_paper_generator.mapper.BookMapper;
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

    private final BookMapper mapper;
    private final BookService bookService;
    private final ChapterService chapterService;
    private final Path root = Paths.get("uploads");
    private final OcrService ocrService;
    private final ChunkService chunkService;

    // Save file
    @Transactional
    public String saveFile(MultipartFile file, String subjectName, String bookName) {

        if (file == null || file.isEmpty()) {
            throw new FileNotAllowedException("File is empty");
        }

        String originalName = Optional.ofNullable(file.getOriginalFilename())
                .orElse("unknown.pdf");

        if (!originalName.toLowerCase().endsWith(".pdf")) {
            throw new FileNotAllowedException("Only PDF allowed");
        }

        try {

            // Ensure directory exists
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            String fileName = UUID.randomUUID() + "_" + originalName;
            Path destination = root.resolve(fileName);

            // Save file safely
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }

            // Save Book in DB
            BookDtoBasic savedBook = bookService.saveBook(
                    new BookDtoWithPath(bookName, subjectName, destination.toString())
            );

            // Chunk after DB save
            chunkBook(destination, savedBook);

            return "File saved successfully";

        } catch (Exception e) {

            // delete file if DB fails
            try {
                Files.deleteIfExists(root);
            } catch (IOException ignored) {}

            throw  new FileNotSavedException("File upload failed");
        }
    }

    // Save chapters
    @Transactional
    public String saveChapters(List<MultipartFile> files, BookDtoBasic bookDtoBasic){

        if (files == null || files.isEmpty()) {
            throw new FileNotAllowedException("No files uploaded");
        }

        // This method is not complete i have to fix it
        BookDtoBasic book = bookService.saveBook(mapper.basicBookDto(bookDtoBasic));

        if (!Files.exists(root)) {
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                throw new FileNotSavedException("Could not create directory");
            }
        }

        List<ChapterDto> chapters = new ArrayList<>();

        for (MultipartFile file : files) {

            String originalName = Optional.ofNullable(file.getOriginalFilename())
                    .orElse("unknown.pdf");

            if (!originalName.toLowerCase().endsWith(".pdf")) {
                throw new FileNotAllowedException("Only PDF allowed");
            }

            try (InputStream inputStream = file.getInputStream()) {
                String fileName = UUID.randomUUID() + "_" + originalName;
                Path destination = root.resolve(fileName);
                Files.copy(inputStream, destination);

                chapters.add(new ChapterDto(fileName, destination.toString(), book));

            } catch (IOException e) {
                throw new FileNotSavedException("File upload failed: " + originalName);
            }
        }

        chapterService.saveChapters(chapters);
        return "Successfully uploaded";
    }


    // Save chapter into existing the book.
    @Transactional
    public String saveChapter(MultipartFile file, long book_id){
        if (file == null || file.isEmpty()) {
            throw new FileNotAllowedException("No files uploaded");
        }

        BookDtoBasic book = bookService.findBookById(book_id);

        if (!Files.exists(root)) {
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                throw new FileNotSavedException("Could not create directory");
            }
        }

        String originalName = Optional.ofNullable(file.getOriginalFilename())
                .orElse("unknown.pdf");

        if (!originalName.toLowerCase().endsWith(".pdf")) {
            throw new FileNotAllowedException("Only PDF allowed");
        }

        try (InputStream inputStream = file.getInputStream()) {
            String fileName = UUID.randomUUID() + "_" + originalName;
            Path destination = root.resolve(fileName);
            Files.copy(inputStream, destination);

            chapterService.saveChapter(new ChapterDto(fileName, destination.toString(), book));
            return "Successfully uploaded";

        } catch (IOException e) {
            throw new FileNotSavedException("File upload failed: " + originalName);
        }
    }

    // Extract text from the digital file.
    public String extractTextFromFile(String filePath){

        Path path = Path.of(filePath);

        if(!Files.exists(path)){
            throw new IllegalArgumentException("file not found");
        }

        try(InputStream inputStream = Files.newInputStream(path)) {
            PDDocument document = PDDocument.load(inputStream);
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



    //Chunk the book into chapters and then further
    private void chunkBook(Path path, BookDtoBasic bookDtoBasic){

        String text;

        try {
            if(isScannedPdf(Files.newInputStream(path))){
                text = extractTextFromScannedFile(path.toString());
            }
            else {
                text = extractTextFromFile(path.toString());
            }
            chunkService.chunkBookIntoChapters(text, bookDtoBasic);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}