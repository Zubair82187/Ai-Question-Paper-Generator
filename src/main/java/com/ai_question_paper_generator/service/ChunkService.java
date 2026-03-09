package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.book_dto.BookDtoBasic;
import com.ai_question_paper_generator.dto.chunk_dto.ChunkDto;
import com.ai_question_paper_generator.mapper.ChunkMapper;
import com.ai_question_paper_generator.repository.ChunkRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Service
public class ChunkService {

    private final TextCleaningService textCleaningService;
    private final ChunkRepository chunkRepository;
    private final ChunkMapper chunkMapper;
    private final ChapterService chapterService;

    @Async
    public void chunkBookIntoChapters(String bookText, BookDtoBasic bookDtoBasic){
        String text = textCleaningService.cleanText(bookText);
        List<String> chapters = splitIntoChapters(text);
    }

    private void saveChapter(String chapterText){
        List<String> chunks = chunkChapter(chapterText);
        long chapter_id = 2;
        for (String chunk : chunks) {
            saveChunk(chunk, chapter_id);
        }
    }

    // Split book text into chapters
    private List<String> splitIntoChapters(String text) {
        String[] chapters = text.split("(?=(Chapter\\s+\\d+|CHAPTER\\s+\\d+|Unit\\s+\\d+|Lesson\\s+\\d+|\\d+\\.\\s+[A-Z]))");
        return Arrays.asList(chapters);
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


    private void saveChunk(String chunk, long chapter_id){
        if (chunk == null || chunk.isBlank()) {
            throw new IllegalArgumentException("Chunk text cannot be empty");
        }
        ChunkDto chunkDto =  new ChunkDto(chunk, chapterService.findChapterById(chapter_id));
        chunkRepository.save(chunkMapper.dtoToChunk(chunkDto));
    }

}
