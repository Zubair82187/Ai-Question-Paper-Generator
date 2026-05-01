package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.chunk_dto.ChunkDto;
import com.ai_question_paper_generator.dto.query_dto.*;
import com.ai_question_paper_generator.exception.NotFoundException;
import com.ai_question_paper_generator.model.question_generation_inputs.ChapterQuery;
import com.ai_question_paper_generator.model.question_generation_inputs.TopicQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

@Service
@AllArgsConstructor
public class QuestionService {

    private final AiClient aiClient;
    private final ChunkService chunkService;
    private final EmbeddingService embeddingService;


    public JsonNode generateFromBook(BookQueryDto queryDto){
        List<ChunkDto> chunks = chunkService.findChunkByBookId(queryDto.getBook_id(), 10);
        Collections.shuffle(chunks);
        chunks = chunks.stream().limit(queryDto.getQuestion_count()).toList();

        return aiClient.generateQuestions(chunks, queryDto);
    }

    public JsonNode shortQuestion(BookShortQuestionQueryDto queryDto){
        List<ChunkDto> chunkList = chunkService.findChunkByBookId(queryDto.getBook_id(), 10);
        Collections.shuffle(chunkList);
        List<ChunkDto> chunks = chunkList.stream()
                .limit(queryDto.getQuestion_count()+queryDto.getQuestion_count()/2)
                .toList();

        return aiClient.shortQuestions(chunks, queryDto.getQuestion_count(), queryDto.getDifficulty());
    }

    public JsonNode longQuestion(BookLongQuestionQueryDto queryDto){
        List<ChunkDto> chunkList = chunkService.findChunkByBookId(queryDto.getBook_id(), 10);
        Collections.shuffle(chunkList);
        List<ChunkDto> chunks = chunkList.stream()
                .limit(queryDto.getQuestion_count()+queryDto.getQuestion_count()/2)
                .toList();

        return aiClient.longQuestions(chunks, queryDto.getQuestion_count(), queryDto.getDifficulty());
    }

    public JsonNode mcqQuestion(BookMcqQuestionQueryDto queryDto){
        List<ChunkDto> chunkList = chunkService.findChunkByBookId(queryDto.getBook_id(), 10);
        Collections.shuffle(chunkList);
        List<ChunkDto> chunks = chunkList.stream()
                .limit(queryDto.getQuestion_count()+queryDto.getQuestion_count()/2)
                .toList();

        return aiClient.mcqQuestions(chunks, queryDto.getQuestion_count(), queryDto.getDifficulty());
    }

    public JsonNode generateMcqFromChapter(ChapterQuery chapterQuery){

        List<ChunkDto> chunks = findChapterChunks(chapterQuery);
        if(chunks.isEmpty()){
            throw new NotFoundException("There is no chapter "+ chapterQuery.getChapterName());
        }

        return aiClient.mcqQuestions(chunks, chapterQuery.getQuestionCount(), chapterQuery.getDifficulty());

    }

    public JsonNode generateShortFromChapter(ChapterQuery chapterQuery){
        List<ChunkDto> chunks = findChapterChunks(chapterQuery);

        if(chunks.isEmpty()){
            throw new NotFoundException("There is no chapter "+ chapterQuery.getChapterName());
        }
        return aiClient.shortQuestions(chunks, chapterQuery.getQuestionCount(), chapterQuery.getDifficulty());
    }

    public JsonNode generateLongFromChapter(ChapterQuery chapterQuery){
        List<ChunkDto> chunks = findChapterChunks(chapterQuery);

        if(chunks.isEmpty()){
            throw new NotFoundException("There is no chapter "+ chapterQuery.getChapterName());
        }
        return aiClient.longQuestions(chunks, chapterQuery.getQuestionCount(), chapterQuery.getDifficulty());
    }

    public JsonNode generateMcqFromTopic(TopicQuery topicQuery){
        List<ChunkDto> chunks = findTopicChunks(topicQuery);
        System.out.println(chunks.size());
        if(chunks.isEmpty()){
            throw new NotFoundException("There is no topic found");
        }
        return aiClient.mcqQuestions(chunks, topicQuery.getQuestionCount(), topicQuery.getDifficulty());
    }

    public JsonNode generateShortFromTopic(TopicQuery topicQuery){
        List<ChunkDto> chunks = findTopicChunks(topicQuery);

        if(chunks.isEmpty()){
            throw new NotFoundException("There is no topic found");
        }
        return aiClient.shortQuestions(chunks, topicQuery.getQuestionCount(), topicQuery.getDifficulty());
    }

    public JsonNode generateLongFromTopic(TopicQuery topicQuery){
        List<ChunkDto> chunks = findTopicChunks(topicQuery);

        if(chunks.isEmpty()){
            throw new NotFoundException("There is no topic found");
        }

        return aiClient.longQuestions(chunks, topicQuery.getQuestionCount(), topicQuery.getDifficulty());
    }

    private List<ChunkDto> findChapterChunks(ChapterQuery chapterQuery){
        JsonNode response= aiClient.generateKeywords(chapterQuery.getChapterName(), chapterQuery.getSubjectName());
        JsonNode keywordsArray = response.get("keywords");

        StringJoiner joiner = new StringJoiner(", ");
        if (keywordsArray != null && keywordsArray.isArray()) {
            for (JsonNode keyword : keywordsArray) {
                joiner.add(keyword.asString());
            }
        }

        String keywordString = joiner.toString();
        List<Double> keywordEmbeddings = embeddingService.generateEmbedding(keywordString);
        List<ChunkDto> chunks = chunkService.findChunkEmbeddingsByBookId(chapterQuery.getBook_id());
        List<ChunkDto> chapterChunks = new ArrayList<>();
        for (ChunkDto chunk : chunks) {
            double similarity = consine(keywordEmbeddings, chunk.getEmbedding());
            if (similarity > 0.5) {
                chapterChunks.add(chunk);
            }
        }
        return chapterChunks;
    }

    private List<ChunkDto> findTopicChunks(TopicQuery topicQuery){
        JsonNode response= aiClient.generateKeywords(topicQuery);
        JsonNode keywordsArray = response.get("keywords");

        StringJoiner joiner = new StringJoiner(", ");
        if (keywordsArray != null && keywordsArray.isArray()) {
            for (JsonNode keyword : keywordsArray) {
                joiner.add(keyword.asString());
            }
        }

        String keywordString = joiner.toString();

        List<Double> keywordEmbeddings = embeddingService.generateEmbedding(keywordString);
        List<ChunkDto> chunks = chunkService.findChunkEmbeddingsByBookId(topicQuery.getBook_id());
        List<ChunkDto> chapterChunks = new ArrayList<>();
        for (ChunkDto chunk : chunks) {
            double similarity = consine(keywordEmbeddings, chunk.getEmbedding());
            if (similarity > 0.1) {
                chapterChunks.add(chunk);
            }
        }
        return chapterChunks;
    }

    private Double consine(List<Double> keywordEmbedding, List<Double> chunkEmbedding){
        if(keywordEmbedding.size() != chunkEmbedding.size()){
            throw new IllegalArgumentException("Embeddings size must be same");
        }

        double normA = 0.0;
        double normB = 0.0;
        double dotProduct = 0.0;

        for(int i=0; i< keywordEmbedding.size(); i++){
            dotProduct += keywordEmbedding.get(i) + chunkEmbedding.get(i);
            normA += Math.pow(keywordEmbedding.get(i), 2);
            normB += Math.pow(chunkEmbedding.get(i), 2);
        }

        if (normA == 0 || normB == 0) {
            return 0.0; // avoid division by zero
        }

        return dotProduct/Math.sqrt(normA) * Math.sqrt(normB);
    }


}
