package com.ai_question_paper_generator.controller;

import com.ai_question_paper_generator.dto.book_dto.BookDtoBasic;
import com.ai_question_paper_generator.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


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

//    @PostMapping(value = "/upload/chapters", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> uploadChapters(@RequestPart("chapters") List<MultipartFile> chapters,
//                                                @RequestPart("book") @Validated BookDtoBasic book){
//        return ResponseEntity.status(HttpStatus.OK).body(service.saveChapters(chapters, book));
//    }


//    @PostMapping(value = "/upload/chapter", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> saveChapter(@RequestPart("chapter")MultipartFile file, @RequestPart long id){
//        return ResponseEntity.status(HttpStatus.OK).body(service.saveChapter(file, id));
//    }
}
