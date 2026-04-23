package com.ai_question_paper_generator.dto.chunk_dto;

import com.ai_question_paper_generator.dto.book_dto.BookDtoWithId;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChunkDto {

    @NotBlank
    private String chunk_text;

    @NotBlank
    private List<Double> embedding;

    private BookDtoWithId  bookDtoWithId;
}
