package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.book_dto.BookDtoWithId;
import com.ai_question_paper_generator.dto.chunk_dto.ChunkDto;
import com.ai_question_paper_generator.exception.NotFoundException;
import com.ai_question_paper_generator.mapper.ChunkMapper;
import com.ai_question_paper_generator.repository.ChunkRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ChunkService {

    private final TextCleaningService textCleaningService;
    private final ChunkRepository chunkRepository;
    private final ChunkMapper chunkMapper;
    private final EmbeddingService embeddingService;



    /*
        Chunk book into chapters then save the metadata of those chapters into database.
        Further chunk those chapters into chapter chunks and then store those chunks into database.
        Also generate embeddings.
     */
    @Async
    public void chunkBook(String bookText, BookDtoWithId bookDtoWithId){
        String text = textCleaningService.cleanText(bookText);
        List<String> chunks = chunkChapter(text);

        for (String chunk : chunks) {
            List<Double> embedding = embeddingService.generateEmbedding(chunk);
            saveChunk(chunk, embedding, bookDtoWithId);
        }
    }

    // Save chunks to database
    private void saveChunk(String chunk, List<Double> embedding, BookDtoWithId bookDtoWithId){
        if (chunk == null || chunk.isBlank()) {
            throw new IllegalArgumentException("Chunk text cannot be empty");
        }
        ChunkDto chunkDto =  new ChunkDto(chunk, embedding, bookDtoWithId);
        chunkRepository.save(chunkMapper.dtoToChunk(chunkDto));
    }

    // Chunk chapter
    private List<String> chunkChapter(String text) {

        String[] words = text.split("\\s+");
        List<String> chunks = new ArrayList<>();

        int start = 0;

        while (start < words.length) {

            int end = Math.min(start + 300, words.length);

            StringBuilder sb = new StringBuilder();

            for (int i = start; i < end; i++) {
                sb.append(words[i]).append(" ");
            }

            chunks.add(sb.toString().trim());

            start += (300 - 50);
        }

        return chunks;
    }

    // Find all chunks
    public List<ChunkDto> findAllChunk() {
        return chunkMapper.toChunkDtoList(chunkRepository.findAll());
    }


    public List<ChunkDto> findChunkByBookId(long bookId, int number_of_chunks) {

        Pageable pageable = PageRequest.of(0, number_of_chunks);
        List<ChunkDto> chunks = chunkMapper.toChunkDtoList(chunkRepository.findChunkByBookId(bookId, pageable));
        if(chunks.isEmpty()){
            throw new NotFoundException("there is no chunk of this book.");
        }
        return chunks;
    }

    public List<ChunkDto> findChunkEmbeddingsByBookId(long book_id){
        List<ChunkDto> chunkDtoList = chunkMapper.toChunkDtoList(chunkRepository.findChunkEmbeddingsByBookId(book_id));
        if(chunkDtoList.isEmpty()){
            throw new NotFoundException("There is no embeddings of this book. ");
        }
        return chunkDtoList;
    }
}
