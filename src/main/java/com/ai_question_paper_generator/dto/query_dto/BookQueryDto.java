package com.ai_question_paper_generator.dto.query_dto;

import com.ai_question_paper_generator.enums.Difficulty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookQueryDto {

    // set book is from which you want to generate questions.
    @NotBlank(message = "book id can not be null.")
    private long book_id;

    // set number of questions you want to generate.
    @NotBlank(message = "number of questions can not be zero.")
    private int question_count;

    // Set number of line for a short answer.
    @NotBlank(message = "number of lines in which question can be answered must be defined")
    private int shortAnswer_lines;

    // Set number of words for a long answer.
    @NotBlank(message = "numbers of words in which question can be answered")
    private int longAnswer_words;

    // set number of mcq.
    @NotBlank(message = "numbers of mcq questions must defined")
    private int number_of_mcq;

    // set number of short questions.
    @NotBlank(message = "numbers of short questions must defined")
    private int number_of_short_question;

    // set number of long question.
    @NotBlank(message = "numbers of long questions must defined")
    private int number_of_long_question;

    // set difficulty.
    @NotBlank(message = "difficulty can not be null")
    private Difficulty difficulty;
}
