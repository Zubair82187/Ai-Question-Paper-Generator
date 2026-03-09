package com.ai_question_paper_generator.service;

public interface AiClient {
    String generate(String prompt);
    String generateChapterName(String text);
}
