package com.ai_question_paper_generator.controller;

import com.ai_question_paper_generator.dto.QuestionRequest;
import com.ai_question_paper_generator.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}