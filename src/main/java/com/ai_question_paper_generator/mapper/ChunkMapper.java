package com.ai_question_paper_generator.mapper;

import com.ai_question_paper_generator.dto.chunk_dto.ChunkDto;
import com.ai_question_paper_generator.model.Chunk;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChunkMapper {
    Chunk dtoToChunk(ChunkDto chunkDto);
    ChunkDto toChunkDto(Chunk chunk);

    List<ChunkDto> toChunkDtoList(List<Chunk> all);
}
