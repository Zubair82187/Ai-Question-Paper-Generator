package com.ai_question_paper_generator.dto.chapter_dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChapterDtoBasic {
    private long id;
    private String chapterName;
    private String path;
}
