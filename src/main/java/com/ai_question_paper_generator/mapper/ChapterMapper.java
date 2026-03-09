package com.ai_question_paper_generator.mapper;

import com.ai_question_paper_generator.dto.chapter_dto.ChapterDto;
import com.ai_question_paper_generator.dto.chapter_dto.ChapterDtoBasic;
import com.ai_question_paper_generator.model.Chapter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface ChapterMapper {
    Chapter toChapter(ChapterDto chapterDto);
    ChapterDto toDto(Chapter chapter);
    ChapterDtoBasic toChapterBasicDto(Chapter chapter);
}
