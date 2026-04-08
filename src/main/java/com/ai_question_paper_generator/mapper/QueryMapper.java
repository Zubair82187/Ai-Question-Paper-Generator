package com.ai_question_paper_generator.mapper;

import com.ai_question_paper_generator.dto.query_dto.BookShortQuestionQueryDto;
import com.ai_question_paper_generator.dto.query_dto.ShortQuestionQueryDto;
import com.ai_question_paper_generator.model.question_generation_inputs.Query;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QueryMapper {
    ShortQuestionQueryDto toShortQuestionQueryDto(Query query);
    Query toQuery(ShortQuestionQueryDto shortQuestionQueryDto);
}
