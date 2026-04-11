package com.ai_question_paper_generator.repository;

import com.ai_question_paper_generator.dto.chunk_dto.ChunkDto;
import com.ai_question_paper_generator.model.Chunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChunkRepository extends JpaRepository<Chunk, Long> {

    List<Chunk> findAllById(Long id);

    @Query("SELECT c FROM Chunk c WHERE c.chapter.id = :chapterId")
    List<Chunk> findChunkByChapterId(long chapterId);

    @Query("SELECT c FROM Chunk c JOIN c.chapter ch WHERE ch.book.id = :bookId")
    List<Chunk> findChunkByBookId(long bookId);
}