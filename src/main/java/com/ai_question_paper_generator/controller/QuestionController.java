package com.ai_question_paper_generator.controller;

import com.ai_question_paper_generator.dto.query_dto.*;
import com.ai_question_paper_generator.model.question_generation_inputs.ChapterQuery;
import com.ai_question_paper_generator.model.question_generation_inputs.TopicQuery;
import com.ai_question_paper_generator.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/questions")
@AllArgsConstructor
public class QuestionController {

    private final QuestionService service;

    @PostMapping("/generate_all_type")
    public ResponseEntity<JsonNode> generateFromBook(@RequestBody BookQueryDto queryDto){
        return ResponseEntity.status(HttpStatus.OK).body(service.generateFromBook(queryDto));
    }


    @PostMapping("/generate_mcq")
    public ResponseEntity<JsonNode> generateMcq(@RequestBody BookMcqQuestionQueryDto queryDto){
        return ResponseEntity.status(HttpStatus.OK).body(service.mcqQuestion(queryDto));
    }

    @PostMapping("/generate_short")
    public ResponseEntity<JsonNode> generateShort(@RequestBody BookShortQuestionQueryDto queryDto){
        return ResponseEntity.status(HttpStatus.OK).body(service.shortQuestion(queryDto));
    }

    @PostMapping("/generate_long")
    public ResponseEntity<JsonNode> generateLong(@RequestBody BookLongQuestionQueryDto queryDto){
        return ResponseEntity.status(HttpStatus.OK).body(service.longQuestion(queryDto));
    }

    @PostMapping("/generate_mcq_from_chapter")
    public ResponseEntity<JsonNode> generateMcqFromChapter(@RequestBody ChapterQuery chapterQuery){
        return ResponseEntity.status(HttpStatus.OK).body(service.generateMcqFromChapter(chapterQuery));
    }

    @PostMapping("/generate_short_from_chapter")
    public ResponseEntity<JsonNode> generateShortFromChapter(@RequestBody ChapterQuery chapterQuery){
        return ResponseEntity.status(HttpStatus.OK).body(service.generateShortFromChapter(chapterQuery));
    }

    @PostMapping("/generate_long_from_chapter")
    public ResponseEntity<JsonNode> generateLongFromChapter(@RequestBody ChapterQuery chapterQuery){
        return ResponseEntity.status(HttpStatus.OK).body(service.generateLongFromChapter(chapterQuery));
    }

    @PostMapping("/generate_mcq_from_topic")
    public ResponseEntity<JsonNode> generateMcqFromTopic(@RequestBody TopicQuery topicQuery){
        return ResponseEntity.status(HttpStatus.OK).body(service.generateMcqFromTopic(topicQuery));
    }

    @PostMapping("/generate_short_from_topic")
    public ResponseEntity<JsonNode> generateShortFromTopic(@RequestBody TopicQuery topicQuery){
        return ResponseEntity.status(HttpStatus.OK).body(service.generateShortFromTopic(topicQuery));
    }

    @PostMapping("/generate_long_from_topic")
    public ResponseEntity<JsonNode> generateLongFromTopic(@RequestBody TopicQuery topicQuery){
        return ResponseEntity.status(HttpStatus.OK).body(service.generateLongFromTopic(topicQuery));
    }

}