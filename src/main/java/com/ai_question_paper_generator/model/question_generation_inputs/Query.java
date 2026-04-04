package com.ai_question_paper_generator.model.question_generation_inputs;

import com.ai_question_paper_generator.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Query {

    // set book is from which you want to generate questions.
    private long book_id;

    // set chapter id from which you want to generate questions.
    private long chapter_id;

    // set number of questions you want to generate.
    private int question_count;

    // Set number of line for a short answer.
    private int shortAnswer_lines;

    // Set number of words for a long answer.
    private int longAnswer_words;

    // set number of mcq.
    private int number_of_mcq;

    // set number of short questions.
    private int number_of_short_question;

    // set number of long question.
    private int number_of_long_question;

    // set difficulty.
    private Difficulty difficulty;
}
