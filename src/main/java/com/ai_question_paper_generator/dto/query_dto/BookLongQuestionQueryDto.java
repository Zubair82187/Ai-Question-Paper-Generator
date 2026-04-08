package com.ai_question_paper_generator.dto.query_dto;

import com.ai_question_paper_generator.enums.Difficulty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookLongQuestionQueryDto {

    // set book is from which you want to generate questions.
    @NotBlank(message = "book id can not be null.")
    private long book_id;

    // set number of questions you want to generate.
    @NotBlank(message = "number of questions can not be zero.")
    private int question_count;

    // Set number of words for a long answer.
    @NotBlank(message = "numbers of words in which question can be answered")
    private int longAnswer_words;

    // set difficulty.
    @NotBlank(message = "difficulty can not be null")
    private Difficulty difficulty;
}
