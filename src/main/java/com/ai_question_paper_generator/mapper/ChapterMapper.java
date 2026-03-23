package com.ai_question_paper_generator.mapper;

import com.ai_question_paper_generator.dto.chapter_dto.ChapterDto;
import com.ai_question_paper_generator.dto.chapter_dto.ChapterDtoBasic;
import com.ai_question_paper_generator.dto.chapter_dto.ChapterDtoWithoutPath;
import com.ai_question_paper_generator.model.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface ChapterMapper {
    Chapter toChapter(ChapterDto chapterDto);
    ChapterDto toDto(Chapter chapter);
    ChapterDtoBasic toChapterBasicDto(Chapter chapter);

    List<ChapterDtoBasic> toChapterBasicDtoList(List<Chapter> all);

    @Mapping(source = "bookDtoWithId", target = "book")
    Chapter toChapter(ChapterDtoWithoutPath chapterDtoWithoutPath);
}
