package com.ai_question_paper_generator.controller;

import com.ai_question_paper_generator.dto.QuestionRequest;
import com.ai_question_paper_generator.dto.query_dto.ShortQuestionQueryDto;
import com.ai_question_paper_generator.model.question_generation_inputs.Query;
import com.ai_question_paper_generator.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@AllArgsConstructor
public class QuestionController {
    private final
    QuestionService service;


    @PostMapping("/generate")
    public String generate(@RequestBody QuestionRequest req) {
        return service.generateQuestions(req.getTopic(), req.getText());
    }

    @PostMapping("/generate_from_chapter")
    public ResponseEntity<String> generate(@RequestBody Query query){
        return ResponseEntity.status(HttpStatus.OK).body(service.generateFromChapter(query));
    }

    @PostMapping("/short_questions_from_chapter")
    public ResponseEntity<Object> shortQuestionFromChapter(@RequestBody ShortQuestionQueryDto shortQuestionQueryDto){
        return ResponseEntity.status(HttpStatus.OK).body(service.shortQuestionsFromChapter(shortQuestionQueryDto));
    }

}