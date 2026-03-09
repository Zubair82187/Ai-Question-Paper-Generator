package com.ai_question_paper_generator.dto.book_dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDtoWithPath {
    private String bookName;
    private String subject;
    private String path;
}
