package com.ai_question_paper_generator.dto.book_dto;

import com.ai_question_paper_generator.model.Chapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String bookName;
    private String subject;
    private String path;
    private List<Chapter> chapters;

    public BookDto(String bookName, String subject, String path) {
        this.bookName = bookName;
        this.subject = subject;
        this.path = path;
    }
}