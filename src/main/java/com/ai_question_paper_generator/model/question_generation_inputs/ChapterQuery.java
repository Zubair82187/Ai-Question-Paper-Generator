package com.ai_question_paper_generator.model.question_generation_inputs;

import com.ai_question_paper_generator.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterQuery {
    private long book_id;
    private String chapterName;
    private String subjectName;
    private int questionCount;
    private Difficulty difficulty;
}
