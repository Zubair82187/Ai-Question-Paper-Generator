package com.ai_question_paper_generator.controller;

import com.ai_question_paper_generator.dto.chapter_dto.ChapterDto;
import com.ai_question_paper_generator.dto.chapter_dto.ChapterDtoBasic;
import com.ai_question_paper_generator.mapper.ChapterMapper;
import com.ai_question_paper_generator.repository.ChapterRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/chapter")
public class ChapterController {

    private ChapterRepository chapterRepository;
    private ChapterMapper chapterMapper;

    @GetMapping("/find_all")
    public ResponseEntity<List<ChapterDtoBasic>> findAllChapter(){
        return ResponseEntity.status(HttpStatus.OK).body(chapterMapper.toChapterBasicDtoList(chapterRepository.findAll()));
    }

    @PostMapping("/save_chapter")
    public ResponseEntity<ChapterDtoBasic> saveChapter(@RequestBody ChapterDto chapterDto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(chapterMapper.toChapterBasicDto(chapterRepository.save(chapterMapper.toChapter(chapterDto))));
    }
}
