package com.ai_question_paper_generator.dto.book_dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoWithId {

    private long id;

    @NotBlank(message = "book name is required")
    private String bookName;

    @NotBlank(message = "subject name is required")
    private String subject;
}
