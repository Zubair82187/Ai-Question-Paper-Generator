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
public class BookLongQuestionQueryDto {

    // set book is from which you want to generate questions.
    @NotNull(message = "book id can not be null.")
    private long book_id;

    // set number of questions you want to generate.
    @NotNull(message = "number of questions can not be zero.")
    @Min(value = 1)
    private int question_count;


    // set difficulty.
    @NotNull(message = "difficulty can not be null")
    private Difficulty difficulty;
}
