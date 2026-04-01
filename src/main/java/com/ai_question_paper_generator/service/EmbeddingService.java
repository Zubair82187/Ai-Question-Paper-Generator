package com.ai_question_paper_generator.service;

import okhttp3.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class EmbeddingService {

    public List<Double> generateEmbedding(String text) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        text = text.replace("\n", " ").replace("\r", " ");

        String body = """
            {
              "model": "nomic-embed-text",
              "prompt": "%s"
            }
            """.formatted(text);

        Request request = new Request.Builder()
                .url("http://localhost:11434/api/embeddings")
                .post(RequestBody.create(
                        body,
                        MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {

            String responseBody = response.body().string();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);

            JsonNode embeddingNode = jsonNode.get("embedding");

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
