package com.ai_question_paper_generator.dto;

import lombok.Data;

@Data
public class QuestionRequest {
    private String topic;
    private String text;
}