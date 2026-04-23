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
public class BookShortQuestionQueryDto {

    // set book is from which you want to generate questions.
    @NotNull(message = "book id can not be null.")
    private Long book_id;

    @Min(value = 1, message = "question count must be greater than 0")
    private int question_count;

    @NotNull(message = "difficulty can not be null")
    private Difficulty difficulty;
}
