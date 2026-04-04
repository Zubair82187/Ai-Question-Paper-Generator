package com.ai_question_paper_generator.dto.query_dto;


import com.ai_question_paper_generator.enums.Difficulty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortQuestionQueryDto {

    // set chapter id from which you want to generate questions.
    @NotBlank(message = "chapter id can not be empty")
    private long chapter_id;

    // set number of questions you want to generate.
    @NotBlank(message = "number of questions must be defined")
    private int question_count;

    // Set number of line for a short answer.
    @NotBlank(message = "number of lines in which question can be answered must be defined")
    private int shortAnswer_lines;

    // set number of short questions.
    @NotBlank(message = "number of short questions you want to generate can not be empty")
    private int number_of_short_question;

    // set difficulty.
    private Difficulty difficulty;
}
