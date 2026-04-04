package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.book_dto.BookDtoBasic;
import com.ai_question_paper_generator.dto.chapter_dto.ChapterDtoBasic;
import com.ai_question_paper_generator.dto.chunk_dto.ChunkDto;
import com.ai_question_paper_generator.dto.query_dto.ShortQuestionQueryDto;
import com.ai_question_paper_generator.model.question_generation_inputs.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class QuestionService {

    private final AiClient aiClient;
    private final BookService bookService;
    private final ChapterService chapterService;
    private final ChunkService chunkService;

    public String generateQuestions(String topic, String text) {

        String prompt = """
        You are an exam question generator.

        Rules:
        - Generate EXACTLY 6 questions
        - No explanation
        - Return ONLY valid JSON
        - No markdown

        Topic: %s

        Content:
        %s
        """.formatted(topic, text);

        String fullResponseString = aiClient.generate(prompt);

        /*
         Converting JSON string into jsonNode tree
         so that we can extract what we want from response
         as of now response string contains a lot of things like response model and others,
         but here we need only response.
        */
        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(fullResponseString);
        String responseText = root.get("response").asString();
        responseText = responseText.replace("```", "").trim();

        return responseText;

    }

    public String summerizeText(String text){
        String prompt = """
                You are a text summerizer.
               \s
                Rules:
                Summerize whole book text into one thousand to two thousand words maximum.
                cover all the chapters\s
                topics from all chapters
                all the excercise if given\s
                all the examples if it's a maths, physics or chemistry book
                So that I can generate questions from this summary without leaving even a single topic from book.
               \s
                Content:
                %s         \s
               \s""".formatted(text);
        String fullResponseString = aiClient.generate(prompt);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root = objectMapper.readTree(fullResponseString);
        String responseText = root.get("response").toString();
        responseText = responseText.replace("```", "").trim();
        return responseText;
    }

    // generate questions from a specific chapter and the questions would be every type like mcq, short and long.
    public String generateFromChapter(Query query){

        List<ChunkDto> chunks = chunkService.findChunkByChapter(query.getChapter_id());
        Collections.shuffle(chunks);
        chunks = chunks.stream().limit(query.getQuestion_count()).toList();

        return aiClient.generateQuestions(chunks, query);
    }

    // Generate short questions from a specific chapter.
    public String shortQuestionsFromChapter(ShortQuestionQueryDto queryDto){
        List<ChunkDto> chunks = chunkService.findChunkByChapter(queryDto.getChapter_id());
        Collections.shuffle(chunks);
        chunks = chunks.stream().limit(queryDto.getQuestion_count()).toList();
        return aiClient.shortQuestionsFromChapter(chunks, queryDto);
    }



}
