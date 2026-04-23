package com.ai_question_paper_generator.dto.query_dto;


import com.ai_question_paper_generator.enums.Difficulty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookMcqQuestionQueryDto {
    // set chapter id from which you want to generate questions.
    private long book_id;

    // set number of questions you want to generate.
    @Min(value = 1, message = "number of questions can not be zero")
    private int question_count;

    // set difficulty.
    @NotNull(message = "difficulty level can not be null")
    private Difficulty difficulty;
}
