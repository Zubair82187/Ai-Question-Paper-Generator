package com.ai_question_paper_generator.repository;

import com.ai_question_paper_generator.model.Chunk;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChunkRepository extends JpaRepository<Chunk, Long> {

    @Query("SELECT c FROM Chunk c WHERE c.book.id = :bookId")
    List<Chunk> findChunkByBookId(@Param("bookId") long bookId, Pageable pageable);

    @Query("SELECT c FROM Chunk c WHERE c.book.id = :bookId")
    List<Chunk> findChunkEmbeddingsByBookId(@Param("bookId") long bookId);
}