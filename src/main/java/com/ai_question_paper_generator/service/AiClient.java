package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.chunk_dto.ChunkDto;
import com.ai_question_paper_generator.dto.query_dto.LongQuestionsQueryDto;
import com.ai_question_paper_generator.dto.query_dto.McqQuestionsQueryDto;
import com.ai_question_paper_generator.dto.query_dto.ShortQuestionQueryDto;
import com.ai_question_paper_generator.model.question_generation_inputs.Query;

import java.util.List;

public interface AiClient {
    String generate(String prompt);
    String generateChapterName(String text);
    String generateQuestions(List<ChunkDto> chunks, Query query);

    String shortQuestionsFromChapter(List<ChunkDto> chunks, ShortQuestionQueryDto queryDto);

    String longQuestionsFromChapter(List<ChunkDto> chunks, LongQuestionsQueryDto queryDto);

    String mcqQuestionsFromChapter(List<ChunkDto> chunks, McqQuestionsQueryDto queryDto);
}
