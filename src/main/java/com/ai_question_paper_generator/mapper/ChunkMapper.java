package com.ai_question_paper_generator.mapper;

import com.ai_question_paper_generator.dto.chunk_dto.ChunkDto;
import com.ai_question_paper_generator.model.Book;
import com.ai_question_paper_generator.model.Chunk;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = BookMapper.class)
public interface ChunkMapper {
    @Mapping(target = "book", source = "bookDtoWithId")
    Chunk dtoToChunk(ChunkDto chunkDto);

    List<ChunkDto> toChunkDtoList(List<Chunk> all);
}
