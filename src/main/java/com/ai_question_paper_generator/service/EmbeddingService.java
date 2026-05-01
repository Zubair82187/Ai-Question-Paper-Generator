package com.ai_question_paper_generator.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class EmbeddingService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.embedding.url}")
    private String geminiEmbeddingUrl;

    public List<Double> generateEmbedding(String text) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        text = text.replace("\n", " ").replace("\r", " ");

        text = text.replace("\u0000", ""); // remove hidden null chars

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode contentNode = mapper.createObjectNode();
        contentNode.put("text", text);

        ObjectNode partsNode = mapper.createObjectNode();
        partsNode.set("parts", mapper.createArrayNode().add(contentNode));

        ObjectNode json = mapper.createObjectNode();
        json.set("content", partsNode);

        String body;

        try {
            body = mapper.writeValueAsString(json);
        } catch (Exception e) {
            throw new RuntimeException("Error creating request body", e);
        }

        String url = geminiEmbeddingUrl + "?key=" + geminiApiKey;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(
                        body,
                        MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {

            String responseBody = null;
            if (response.body() != null) {
                responseBody = response.body().string();
            }
            JsonNode jsonNode = mapper.readTree(responseBody);

            if (jsonNode.has("error")) {
                throw new RuntimeException("Gemini API error: " +
                        jsonNode.get("error").get("message").asString());
            }

            JsonNode embeddingNode = jsonNode
                    .get("embedding")
                    .get("values");

            if (embeddingNode == null || !embeddingNode.isArray()) {
                throw new RuntimeException("Invalid embedding response: " + responseBody);
            }

            List<Double> embedding = new ArrayList<>();
            for (JsonNode node : embeddingNode) {
                embedding.add(node.asDouble());
            }

            return embedding;

        } catch (Exception e) {
            throw new RuntimeException("Embedding error", e);
        }
    }


}
