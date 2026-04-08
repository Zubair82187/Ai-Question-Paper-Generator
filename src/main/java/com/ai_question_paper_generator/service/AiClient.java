package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.chunk_dto.ChunkDto;
import com.ai_question_paper_generator.dto.query_dto.*;
import com.ai_question_paper_generator.model.question_generation_inputs.Query;

import java.util.List;

public interface AiClient {
    String generate(String prompt);
    String generateChapterName(String text);
    String generateQuestions(List<ChunkDto> chunks, Query query);

    String shortQuestions(List<ChunkDto> chunks, ShortQuestionQueryDto queryDto);

    String longQuestions(List<ChunkDto> chunks, LongQuestionsQueryDto queryDto);

    String mcqQuestions(List<ChunkDto> chunks, McqQuestionsQueryDto queryDto);


    String shortQuestions(List<ChunkDto> chunks, BookShortQuestionQueryDto queryDto);

    String longQuestions(List<ChunkDto> chunks, BookLongQuestionQueryDto queryDto);

    String mcqQuestions(List<ChunkDto> chunks, BookMcqQuestionQueryDto queryDto);

    String generateQuestions(List<ChunkDto> chunks, BookQueryDto queryDto);
}
