package com.ai_question_paper_generator.dto.query_dto;

import com.ai_question_paper_generator.enums.Difficulty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookQueryDto {

    // set book is from which you want to generate questions.
    private long book_id;

    // set number of questions you want to generate.
    @Min(value = 3, message = "minimum questions should be at least 3")
    private int question_count;

    // set number of mcq.
    @Min(value = 1, message = "numbers of mcq questions can not be zero")
    private int number_of_mcq;

    // set number of short questions.
    @Min(value = 1, message = "numbers of short questions can not be zero")
    private int number_of_short_question;

    // set number of long question.
    @Min(value = 1, message = "numbers of long questions can not be zero")
    private int number_of_long_question;

    // set difficulty.
    @NotNull(message = "difficulty can not be null")
    private Difficulty difficulty;
}
