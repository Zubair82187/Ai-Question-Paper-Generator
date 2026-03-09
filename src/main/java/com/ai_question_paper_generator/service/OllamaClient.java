package com.ai_question_paper_generator.service;

import okhttp3.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Profile("ollama")
public class OllamaClient implements AiClient{

    private static final String OLLAMA_URL =
            "http://localhost:11434/api/generate";

    @Override
    public String generate(String prompt) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();


        prompt = prompt.replace("\n", " ").replace("\r", " ");

        String body = """
                        {
                          "model": "llama3.1:8b",
                          "prompt": "%s",
                          "stream": false
                        }
                        """.formatted(prompt);


        Request request = new Request.Builder()
                .url(OLLAMA_URL)
                .post(RequestBody.create(
                        body,
                        MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            throw new RuntimeException("Ollama error", e);
        }
    }

    @Override
    public String generateChapterName(String text) {
        return "chapter name";
    }
}
