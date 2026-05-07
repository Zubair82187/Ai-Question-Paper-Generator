package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.chunk_dto.ChunkDto;
import com.ai_question_paper_generator.dto.query_dto.*;
import com.ai_question_paper_generator.enums.Difficulty;
import com.ai_question_paper_generator.model.question_generation_inputs.ChapterQuery;
import com.ai_question_paper_generator.model.question_generation_inputs.Query;
import com.ai_question_paper_generator.model.question_generation_inputs.TopicQuery;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import tools.jackson.databind.JsonNode;

import java.util.List;

public interface AiClient {

    JsonNode shortQuestions(List<ChunkDto> chunks, int questionCount, Difficulty difficulty);

    JsonNode longQuestions(List<ChunkDto> chunks, int questionCount, Difficulty difficulty);

    JsonNode mcqQuestions(List<ChunkDto> chunks, int questionCount, Difficulty difficulty);

    JsonNode generateQuestions(List<ChunkDto> chunks, BookQueryDto queryDto);

    JsonNode generateKeywords(String chapterName, String subjectName);

    JsonNode generateKeywords(TopicQuery topicQuery);
}
