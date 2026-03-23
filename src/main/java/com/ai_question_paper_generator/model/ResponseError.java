package com.ai_question_paper_generator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseError {
    private LocalDateTime localDateTime;
    private String message;
    private int status;
    private Map<String, String> errors;

    public ResponseError(LocalDateTime localDateTime, String message, int status) {
        this.localDateTime = localDateTime;
        this.message = message;
        this.status = status;
    }
}