package com.ai_question_paper_generator.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
public class QuestionService {
    private final AiClient aiClient;

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
        String responseText = root.get("response").asText();
        responseText = responseText.replace("```", "").trim();

        return responseText;

    }

    public String summerizeText(String text){
        String prompt = """
                You are a text summerizer.
                
                Rules:
                Summerize whole book text into one thousand to two thousand words maximum.
                cover all the chapters 
                topics from all chapters
                all the excercise if given 
                all the examples if it's a maths, physics or chemistry book
                So that I can generate questions from this summary without leaving even a single topic from book.
                
                Content:
                %s          
                """.formatted(text);
        String fullResponseString = aiClient.generate(prompt);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root = objectMapper.readTree(fullResponseString);
        String responseText = root.get("response").toString();
        responseText = responseText.replace("```", "").trim();
        return responseText;
    }
}
