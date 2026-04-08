package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.chunk_dto.ChunkDto;
import com.ai_question_paper_generator.dto.query_dto.BookLongQuestionQueryDto;
import com.ai_question_paper_generator.dto.query_dto.BookMcqQuestionQueryDto;
import com.ai_question_paper_generator.dto.query_dto.BookQueryDto;
import com.ai_question_paper_generator.dto.query_dto.BookShortQuestionQueryDto;
import com.ai_question_paper_generator.model.question_generation_inputs.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class BookQuestionGeneration {

    private final AiClient aiClient;
    private final ChunkService chunkService;

    public String generateFromBook(BookQueryDto queryDto){
        List<ChunkDto> chunks = chunkService.findChunkByBookId(queryDto.getBook_id());
        Collections.shuffle(chunks);
        chunks = chunks.stream().limit(queryDto.getQuestion_count()).toList();

        return aiClient.generateQuestions(chunks, queryDto);
    }

    public String shortQuestion(BookShortQuestionQueryDto queryDto){
        List<ChunkDto> chunkList = chunkService.findChunkByBookId(queryDto.getBook_id());
        Collections.shuffle(chunkList);
        List<ChunkDto> chunks = chunkList.stream()
                .limit(queryDto.getQuestion_count()+queryDto.getQuestion_count()/2)
                .toList();

        return aiClient.shortQuestions(chunks, queryDto);
    }

    public String longQuestion(BookLongQuestionQueryDto queryDto){
        List<ChunkDto> chunkList = chunkService.findChunkByBookId(queryDto.getBook_id());
        Collections.shuffle(chunkList);
        List<ChunkDto> chunks = chunkList.stream()
                .limit(queryDto.getQuestion_count()+queryDto.getQuestion_count()/2)
                .toList();

        return aiClient.longQuestions(chunks, queryDto);
    }

    public String mcqQuestion(BookMcqQuestionQueryDto queryDto){
        List<ChunkDto> chunkList = chunkService.findChunkByBookId(queryDto.getBook_id());
        Collections.shuffle(chunkList);
        List<ChunkDto> chunks = chunkList.stream()
                .limit(queryDto.getQuestion_count()+queryDto.getQuestion_count()/2)
                .toList();

        return aiClient.mcqQuestions(chunks, queryDto);
    }

}
