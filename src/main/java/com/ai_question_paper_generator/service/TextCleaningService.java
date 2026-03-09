package com.ai_question_paper_generator.service;

import org.springframework.stereotype.Service;

@Service
public class TextCleaningService {

    public String cleanText(String rawText){
        String text = rawText;

        text = normalizeLineBreak(text);
        text = removePageNumbers(text);
        text = fixBrokenWords(text);
        text = removeExtraSpace(text);
        return text.trim();
    }

    // Normalize unwanted line break
    private String normalizeLineBreak(String text) {
        return text.replaceAll("\\r\\n", "\n")
                .replaceAll("\\r", "\n");
    }

    // Remove page numbers
    private String removePageNumbers(String text) {
        return text.replaceAll("(?m)^\\s*\\d{1,4}\\s*$", "");
    }

    // Fix broken words that ocr extract from file
    private String fixBrokenWords(String text) {
        return text.replaceAll("-\\n\\s*", "");
    }

    // Remove extra unwanted space
    private String removeExtraSpace(String text) {
        return text.replaceAll("[ \\t]+", " ")
                .replaceAll("\\n{2,}", "\n\n");
    }

}
