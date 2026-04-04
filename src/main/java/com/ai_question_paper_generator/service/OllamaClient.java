package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.chunk_dto.ChunkDto;
import com.ai_question_paper_generator.dto.query_dto.LongQuestionsQueryDto;
import com.ai_question_paper_generator.dto.query_dto.McqQuestionsQueryDto;
import com.ai_question_paper_generator.dto.query_dto.ShortQuestionQueryDto;
import com.ai_question_paper_generator.exception.NoResponseFound;
import com.ai_question_paper_generator.model.question_generation_inputs.Query;
import okhttp3.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Profile("ollama")
public class OllamaClient implements AiClient{

    private static final String OLLAMA_URL =
            "http://localhost:11434/api/generate";


    //This is s test method to test about our ollama is working or not. I have to delete it.
    @Override
    public String generate(String prompt) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();


        prompt = prompt.replace("\n", " ").replace("\r", " ");



        // Define request body for ollama
        String body = """
                        {
                          "model": "llama3.1:8b",
                          "prompt": "%s",
                          "stream": false
                        }
                        """.formatted(prompt);


        // Make api request
        Request request = new Request.Builder()
                .url(OLLAMA_URL)
                .post(RequestBody.create(
                        body,
                        MediaType.parse("application/json")))
                .build();

        // Handling response
        try (Response response = client.newCall(request).execute()) {

            if(response.body() != null){
                return response.body().string();
            }
            else{
                throw new NoResponseFound("response body is null");
            }
        } catch (Exception e) {
            throw new NoResponseFound("IO Exception while calling model API: " + e.getMessage()+" "+ e);
        }
    }

    @Override
    public String generateChapterName(String text) {
        return "chapter name";
    }

    @Override
    public String generateQuestions(List<ChunkDto> chunks, Query query) {
        return generateResponse(prompt(chunks, query));
    }

    @Override
    public String shortQuestionsFromChapter(List<ChunkDto> chunks, ShortQuestionQueryDto queryDto) {
        return generateResponse(shortQuestionFromChapterPrompt(chunks, queryDto));
    }

    @Override
    public String longQuestionsFromChapter(List<ChunkDto> chunks, LongQuestionsQueryDto queryDto) {
        return generateResponse(longQuestionFromChapterPrompt(chunks, queryDto));
    }

    @Override
    public String mcqQuestionsFromChapter(List<ChunkDto> chunks, McqQuestionsQueryDto queryDto) {
        return generateResponse(mcqQuestionFromChapterPrompt(chunks, queryDto));
    }

    private String shortQuestionFromChapterPrompt(List<ChunkDto> chunks, ShortQuestionQueryDto queryDto){
        // Merge all chunks as a single string
        String content = chunks.stream()
                .map(ChunkDto::getChunk_text)
                .reduce("", (a, b) -> a + "\n" + b);

        String types_of_questions = """
                    - %d Short Answer Questions
                    - %s difficulty
                """.formatted(queryDto.getQuestion_count(),
                queryDto.getDifficulty());

        return """
                    You are an expert academic question paper generator.
                    Your task is to generate a complete exam question paper strictly based on the provided content.
                    INPUT:
                    Content will be given below.
                    You must generate:
                    %s
                   
                    RULES:
                    - Use ONLY the given content.
                    - Do NOT add external knowledge.
                    - Do NOT hallucinate facts.
                    - Ensure questions cover all important parts of the content.
                    - Avoid repetition.
                    - Short answer question can be answered in %d lines at least.
                   
                    SHORT ANSWER FORMAT:
                    Q1. Question
                  
                    CONTENT:
                    %s
                  
                    Generate the question paper now.
                   """.formatted(
                types_of_questions,
                queryDto.getShortAnswer_lines(),
                content
        );
    }

    private String prompt(List<ChunkDto> chunks, Query query){

        // Merge all chunks as a single string
        String content = chunks.stream()
                .map(ChunkDto::getChunk_text)
                .reduce("", (a, b) -> a + "\n" + b);

        String types_of_questions = """
                    - %d Multiple Choice Questions (MCQs)
                    - %d Short Answer Questions
                    - %d Long Answer Questions
                """.formatted(query.getNumber_of_mcq(),
                query.getNumber_of_short_question(),
                query.getNumber_of_long_question()
        );


        return  """
                    You are an expert academic question paper generator.
                    Your task is to generate a complete exam question paper strictly based on the provided content.
                    INPUT:
                    Content will be given below.
                    You must generate:
                    %s
                   
                    RULES:
                    - Use ONLY the given content.
                    - Do NOT add external knowledge.
                    - Do NOT hallucinate facts.
                    - Ensure questions cover all important parts of the content.
                    - Avoid repetition.
                    - Short answer question can be answered in %d lines at least.
                    - Long answer question can be answered in %d words at least.
                   
                    MCQ FORMAT:
                    Q1. Question
                    A. Option
                    B. Option
                    C. Option
                    D. Option
                   
                    SHORT ANSWER FORMAT:
                    Q1. Question
                  
                    LONG ANSWER FORMAT:
                    Q1. Question
                  
                    CONTENT:
                    %s
                  
                    Generate the question paper now.
                   """.formatted(
                types_of_questions,
                query.getShortAnswer_lines(),
                query.getLongAnswer_words(),
                content
        );
    }

    private String longQuestionFromChapterPrompt(List<ChunkDto> chunks, LongQuestionsQueryDto queryDto){

        // Merge all chunks as a single string
        String content = chunks.stream()
                .map(ChunkDto::getChunk_text)
                .reduce("", (a, b) -> a + "\n" + b);

        String types_of_questions = """
                    - %d Long Answer Questions
                    - %s difficulty
                """.formatted(queryDto.getQuestion_count(),
                queryDto.getDifficulty());

        return """
                    You are an expert academic question paper generator.
                    Your task is to generate a complete exam question paper strictly based on the provided content.
                    INPUT:
                    Content will be given below.
                    You must generate:
                    %s
                   
                    RULES:
                    - Use ONLY the given content.
                    - Do NOT add external knowledge.
                    - Do NOT hallucinate facts.
                    - Ensure questions cover all important parts of the content.
                    - Avoid repetition.
                    - Long answer question can be answered in %d words at least.
                   
                    LONG ANSWER FORMAT:
                    Q1. Question
                  
                    CONTENT:
                    %s
                  
                    Generate the question paper now.
                   """.formatted(
                types_of_questions,
                queryDto.getLongAnswer_words(),
                content
        );
    }

    private String mcqQuestionFromChapterPrompt(List<ChunkDto> chunks, McqQuestionsQueryDto queryDto){

        // Merge all chunks as a single string
        String content = chunks.stream()
                .map(ChunkDto::getChunk_text)
                .reduce("", (a, b) -> a + "\n" + b);

        String types_of_questions = """
                    - %d Multiple Choice Questions (MCQs)
                    - %s Difficulty
                """.formatted(queryDto.getQuestion_count(),
                queryDto.getDifficulty()
        );


        return  """
                    You are an expert academic question paper generator.
                    Your task is to generate a complete exam question paper strictly based on the provided content.
                    INPUT:
                    Content will be given below.
                    You must generate:
                    %s
                   
                    RULES:
                    - Use ONLY the given content.
                    - Do NOT add external knowledge.
                    - Do NOT hallucinate facts.
                    - Ensure questions cover all important parts of the content.
                    - Avoid repetition.
                   
                    MCQ FORMAT:
                    Q1. Question
                    A. Option
                    B. Option
                    C. Option
                    D. Option
                   
                    CONTENT:
                    %s
                  
                    Generate the question paper now.
                   """.formatted(
                types_of_questions,
                content
        );
    }

    private String generateResponse(String prompt){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        String body;
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("model", "llama3.1:8b");
            requestMap.put("prompt", prompt);
            requestMap.put("stream", false);

            body = mapper.writeValueAsString(requestMap);

        } catch (Exception e) {
            throw new RuntimeException("Error creating JSON body", e);
        }

        // Make an api request
        Request request = new Request.Builder()
                .url(OLLAMA_URL)
                .post(RequestBody.create(
                        body,
                        MediaType.parse("application/json")))
                .build();

        // response handling
        try(Response response = client.newCall(request).execute()){

            if (response.body() != null) {
                return response.body().string();
            }
            else{
                throw new NoResponseFound("response body is empty");
            }
        } catch (IOException e) {
            throw new NoResponseFound("IO Exception while calling model API: " + e.getMessage()+" "+ e);
        }
    }
}
