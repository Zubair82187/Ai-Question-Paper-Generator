package com.ai_question_paper_generator.controller;

import com.ai_question_paper_generator.dto.chunk_dto.ChunkDto;
import com.ai_question_paper_generator.service.ChunkService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/chunk")
public class ChunkController {

    private final ChunkService chunkService;

    @GetMapping("/find_all_chunk")
    public ResponseEntity<List<ChunkDto>> findAllChunk(){
        return ResponseEntity.status(HttpStatus.OK).body(chunkService.findAllChunk());
    }

    @GetMapping("/find_chapter_chunk")
    public ResponseEntity<List<ChunkDto>> findAllChunks(@RequestParam long id){
        return ResponseEntity.status(HttpStatus.OK).body(chunkService.findAllChunk(id));
    }

}
