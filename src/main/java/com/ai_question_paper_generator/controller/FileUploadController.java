package com.ai_question_paper_generator.controller;

import com.ai_question_paper_generator.dto.book_dto.BookDtoBasic;
import com.ai_question_paper_generator.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class FileUploadController {

    private FileUploadService service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadBook(@RequestParam MultipartFile pdf,
                                            @RequestPart @Valid BookDtoBasic bookDtoBasic){
        return ResponseEntity.status(HttpStatus.OK).body(service.saveFile(pdf, bookDtoBasic));
    }
}
