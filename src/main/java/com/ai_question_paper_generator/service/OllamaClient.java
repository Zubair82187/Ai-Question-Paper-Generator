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

    private static final String OLLAMA_URL =
            "http://localhost:11434/api/generate";

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqApiUrl;



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
    public JsonNode shortQuestions(List<ChunkDto> chunks, int questionCount, Difficulty difficulty) {
        return generateResponse(shortQuestionPrompt(chunks, questionCount, difficulty));
    }

    @Override
    public JsonNode longQuestions(List<ChunkDto> chunks, int questionCount, Difficulty difficulty) {
        return generateResponse(longQuestionPrompt(chunks, questionCount, difficulty));
    }

    @Override
    public JsonNode mcqQuestions(List<ChunkDto> chunks, int questionCount, Difficulty difficulty) {
        return generateResponse(mcqQuestionPrompt(chunks, questionCount, difficulty));
    }

    @Override
    public JsonNode generateQuestions(List<ChunkDto> chunks, BookQueryDto queryDto) {
        return generateResponse(bookQuestionPrompt(chunks, queryDto));
    }

    @Override
    public JsonNode generateKeywords(String chapterName, String subjectName) {
        String prompt = """
            Generate exactly 10 unique concise keywords for the chapter "%s" of subject "%s"
            Output:
                - Return ONLY raw JSON
                - No answers, explanations, or extra text
        """.formatted(chapterName, subjectName);

        return generateResponse(prompt);
    }

    @Override
    public JsonNode generateKeywords(TopicQuery topicQuery) {
        String prompt = """
            Generate exactly 100 unique concise keywords for the topic "%s" of subject "%s"
            Output:
                - Return ONLY valid JSON object
                - No answers, explanations, or extra text
        """.formatted(topicQuery.getTopic(), topicQuery.getSubjectName());

        return generateResponse(prompt);
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
    private JsonNode generateResponse(String prompt){
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
            requestMap.put("temperature", 0.7);
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

    // Json validation
    private boolean isValid(JsonNode node) {
        if (node == null || !node.isArray()) return false;

        for (JsonNode item : node) {
            if (!item.has("title") || !item.has("description")) {
                return false;
            }
        }
        return true;
    }

    public JsonNode generateWithRetry(String prompt) {
        int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            JsonNode result = generateResponse(prompt);

            if (isValid(result)) {
                return result;
            }

            // strengthen instruction on retry
            prompt += "\n\nIMPORTANT: Return ONLY valid JSON. No text.";
        }

        throw new RuntimeException("Failed after " + maxRetries + " attempts");
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
