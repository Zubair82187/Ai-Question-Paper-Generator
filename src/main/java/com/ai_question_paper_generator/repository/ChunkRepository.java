package com.ai_question_paper_generator.repository;

import com.ai_question_paper_generator.model.Chunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChunkRepository extends JpaRepository<Chunk, Long> {

    List<Chunk> findAllById(Long id);

    List<Chunk> findChunkByChapterId(long chapterId);
}