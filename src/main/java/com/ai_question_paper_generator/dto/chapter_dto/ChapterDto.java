package com.ai_question_paper_generator.dto.chapter_dto;

import com.ai_question_paper_generator.dto.book_dto.BookDtoBasic;
import com.ai_question_paper_generator.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDto {

    private String chapterName;
    private String path;
    private BookDtoBasic book;
}
