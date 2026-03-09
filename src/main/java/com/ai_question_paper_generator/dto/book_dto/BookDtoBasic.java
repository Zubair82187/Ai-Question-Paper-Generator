package com.ai_question_paper_generator.dto.book_dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoBasic {
    private String bookName;
    private String subject;
}
