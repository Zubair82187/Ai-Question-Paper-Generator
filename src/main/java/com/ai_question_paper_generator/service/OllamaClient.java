package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.chunk_dto.ChunkDto;
import com.ai_question_paper_generator.dto.query_dto.*;
import com.ai_question_paper_generator.enums.Difficulty;
import com.ai_question_paper_generator.exception.NoResponseFound;
import com.ai_question_paper_generator.model.question_generation_inputs.TopicQuery;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Profile("ollama")
public class OllamaClient implements AiClient{

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqApiUrl;

    @Override
    public JsonNode shortQuestions(List<ChunkDto> chunks, int questionCount, Difficulty difficulty) {
        return generateResponse(shortQuestionPrompt(chunks, questionCount, difficulty), 0.7f);
    }

    @Override
    public JsonNode longQuestions(List<ChunkDto> chunks, int questionCount, Difficulty difficulty) {
        return generateResponse(longQuestionPrompt(chunks, questionCount, difficulty), 0.7f);
    }

    @Override
    public JsonNode mcqQuestions(List<ChunkDto> chunks, int questionCount, Difficulty difficulty) {
        return generateResponse(mcqQuestionPrompt(chunks, questionCount, difficulty), 0.7f);
    }

    @Override
    public JsonNode generateQuestions(List<ChunkDto> chunks, BookQueryDto queryDto) {
        return generateResponse(bookQuestionPrompt(chunks, queryDto), 0.7f);
    }

    @Override
    public JsonNode generateKeywords(String chapterName, String subjectName) {
        String prompt = """
            Generate exactly 10 unique concise keywords for the chapter "%s" of subject "%s"
            Output:
                - Return ONLY raw JSON
                - No answers, explanations, or extra text
        """.formatted(chapterName, subjectName);

        return generateResponse(prompt, 0.0f);
    }

    @Override
    public JsonNode generateKeywords(TopicQuery topicQuery) {
        String prompt = """
            Generate exactly 20 unique concise keywords for the topic "%s" of subject "%s"
            Output:
                - Return ONLY valid JSON object
                - No answers, explanations, or extra text
        """.formatted(topicQuery.getTopic(), topicQuery.getSubjectName());

        return generateResponse(prompt, 0.0f);
    }


    // Prompt methods

    // This method return a prompt to generate questions of multiple type like mcq, short answer and long answer type questions
    private String bookQuestionPrompt(List<ChunkDto> chunks, BookQueryDto query){
        // Merge all chunks as a single string
        String content = chunks.stream()
                .map(ChunkDto::getChunk_text)
                .collect(Collectors.joining("\n\n"));

        String rules = """
                Use ONLY given content. No assumptions.
                Skip unclear topics.
                Questions must be clear and exam-ready.
                MCQs: 4 plausible options from content.
                Short/Long: test understanding.
                No repetition.
                
                Output ONLY raw JSON. No extra text.
                """;

        return """
                Generate:
                - %d total questions
                - %d MCQs (4 options each)
                - %d short answer
                - %d long answer
                Difficulty: %s
                
                %s
                
                Format:
                {
                  "mcq": [],
                  "short_answer_question": [],
                  "long_answer_question": []
                }
                
                Content:
                %s
                """.formatted(
                query.getQuestion_count(),
                query.getNumber_of_mcq(),
                query.getNumber_of_short_question(),
                query.getNumber_of_long_question(),
                query.getDifficulty(),
                rules,
                content
        );
    }

    // This method return a prompt to generate mcq questions
    private String mcqQuestionPrompt(List<ChunkDto> chunks, int questionCount, Difficulty difficulty){
        // Merge all chunks as a single string
        String content = chunks.stream()
                .map(ChunkDto::getChunk_text)
                .collect(Collectors.joining("\n\n"));

        String rules = """
                You are an expert exam question generator.
                
                Rules:
                - Use ONLY the given content (no assumptions or extra info)
                - Skip unclear or insufficient topics
                - Questions must be clear, complete, and exam-ready
                - MCQs must have 4 realistic, plausible options from the content
                - Do NOT repeat questions
                
                Output:
                - Return ONLY raw JSON
                - No answers, explanations, or extra text
                """;

        return """
                You are a teacher creating a question paper.
                
                Generate exactly %d MCQs (4 options each)
                Difficulty: %s
                
                %s
                
                Format:
                {
                  "mcq": []
                }
                
                Content:
                %s
                """.formatted(
                questionCount,
                difficulty,
                rules,
                content
        );
    }

    // This method return a prompt to generate long answer questions
    private String longQuestionPrompt(List<ChunkDto> chunks, int questionCount, Difficulty difficulty){
        // Merge all chunks as a single string
        String content = chunks.stream()
                .map(ChunkDto::getChunk_text)
                .collect(Collectors.joining("\n\n"));

        String rules = """
                Use ONLY given content. No assumptions or extra info.
                Skip unclear topics.
                Questions must be clear, complete, and exam-oriented.
                Long answers should test understanding, not memorization.
                Do not repeat questions.
                
                Output ONLY raw JSON. No answers, explanations, or extra text.
                """;

        return """
                Generate exactly %d long answer questions
                Difficulty: %s
                
                %s
                
                Format:
                {
                  "long_answer_type_questions": []
                }
                
                Content:
                %s
                """.formatted(
                questionCount,
                difficulty,
                rules,
                content
        );
    }

    // This method return a prompt to generate short answer questions
    private String shortQuestionPrompt(List<ChunkDto> chunks, int questionCount, Difficulty difficulty) {
        // Merge all chunks as a single string
        String content = chunks.stream()
                .map(ChunkDto::getChunk_text)
                .collect(Collectors.joining("\n\n"));

        String rules = """
                Use ONLY given content. No assumptions.
                Skip unclear topics.
                Questions must be clear, complete, and test understanding.
                Do not repeat.
                
                Output ONLY raw JSON. No extra text.
                """;

        return """
                Generate exactly %d short answer questions
                Difficulty: %s
                
                %s
                
                Format:
                {
                  "short_answer_type_questions": []
                }
                
                Content:
                %s
                """.formatted(
                questionCount,
                difficulty,
                rules,
                content
        );
    }

    // this return a jsonNode that contain generate questions
    private JsonNode generateResponse(String prompt, float temperature){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        String body;
        ObjectMapper mapper = new ObjectMapper();

        try {

            Map<String, Object> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are a JSON API. Return only raw JSON, nothing else. No markdown, no backticks, no explanation.");

            Map<String, Object> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);

            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("model", "llama-3.3-70b-versatile");
            requestMap.put("messages", List.of(systemMessage, userMessage));
            requestMap.put("temperature", temperature);
            requestMap.put("max_tokens", 1000);

            body = mapper.writeValueAsString(requestMap);

        } catch (Exception e) {
            throw new RuntimeException("Error creating JSON body", e);
        }

        // Make an api request
        Request request = new Request.Builder()
                .url(groqApiUrl)
                .addHeader("Authorization", "Bearer " + groqApiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(
                        body,
                        MediaType.parse("application/json")))
                .build();

        // response handling
        try (Response response = client.newCall(request).execute()) {

            if(response.body() != null){


                // remove ollama wrapper
                String rawResponse = response.body().string();

                JsonNode root = mapper.readTree(rawResponse);
                // check for groq api error
                if(root.has("error")){
                    throw new RuntimeException("Groq API error: " + root.get("error").get("message").asString());
                }

                JsonNode responseNode = root.get("choices")
                        .get(0)
                        .get("message")
                        .get("content");

                if (responseNode == null || responseNode.isNull()) {
                    throw new RuntimeException("Missing 'response' field: " + rawResponse);
                }

                String llmResponse = responseNode.asString();

                // extract JSON block
                String cleaned = extractJson(llmResponse);

                cleaned = fixLlmJsonArray(cleaned);
                // parse string to jsonNode
                return mapper.readTree(cleaned);
            }
            else{
                throw new NoResponseFound("response body is null");
            }
        } catch (Exception e) {
            throw new NoResponseFound("IO Exception while calling model API: " + e.getMessage()+" "+ e);
        }
    }

    // helper method to extract response from llm response
    private String extractJson(String llmResponse) {
        int objStart = llmResponse.indexOf("{");
        int objEnd = llmResponse.lastIndexOf("}");

        int arrStart = llmResponse.indexOf("[");
        int arrEnd = llmResponse.lastIndexOf("]");

        // Case 1: JSON Object
        if (objStart != -1 && objEnd != -1) {
            return llmResponse.substring(objStart, objEnd + 1);
        }

        // Case 2: JSON Array
        if (arrStart != -1 && arrEnd != -1) {
            return llmResponse.substring(arrStart, arrEnd + 1);
        }
        throw new RuntimeException("No JSON found in LLM response");
    }

    private String fixLlmJsonArray(String raw) {
        String trimmed = raw.trim();

        // If it looks like {"a", "b"} instead of ["a", "b"]
        if (trimmed.startsWith("{") && !trimmed.contains(":")) {
            // Replace curly braces with square brackets
            trimmed = "[" + trimmed.substring(1, trimmed.length() - 1) + "]";
        }

        return trimmed;
    }

}
