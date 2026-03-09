package com.ai_question_paper_generator.dto.chunk_dto;

import com.ai_question_paper_generator.model.Chapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChunkDto {
    private String chunk_text;
    private Chapter chapter;
}
