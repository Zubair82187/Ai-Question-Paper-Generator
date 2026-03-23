package com.ai_question_paper_generator.dto.chapter_dto;


import com.ai_question_paper_generator.dto.book_dto.BookDtoWithId;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDtoWithoutPath {

    @NotBlank(message = "chapter name can not be null or empty")
    private String chapterName;

    @NotBlank(message = "book dto can not be null or empty")
    private BookDtoWithId bookDtoWithId;
}
