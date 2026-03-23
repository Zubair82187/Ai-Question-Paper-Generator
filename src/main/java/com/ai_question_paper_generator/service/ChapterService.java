package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.chapter_dto.ChapterDto;
import com.ai_question_paper_generator.dto.chapter_dto.ChapterDtoBasic;
import com.ai_question_paper_generator.dto.chapter_dto.ChapterDtoWithoutPath;
import com.ai_question_paper_generator.exception.NotFoundException;
import com.ai_question_paper_generator.mapper.ChapterMapper;
import com.ai_question_paper_generator.model.Chapter;
import com.ai_question_paper_generator.repository.ChapterRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final ChapterMapper chapterMapper;

    public ChapterDtoBasic saveChapter(ChapterDto chapter){
        return chapterMapper.toChapterBasicDto(chapterMapper.toChapter(chapter));
    }

    @Async
    public void saveChapters(List<ChapterDto> list){

        for (ChapterDto chapterDto : list) {
            chapterRepository.save(chapterMapper.toChapter(chapterDto));
        }

    }


    // Find all chapters
    public List<ChapterDtoBasic> findAllChapters() {
        return chapterRepository.findAll().stream().map(chapterMapper::toChapterBasicDto)
                .collect(Collectors.toList());
    }



    // Extract chapter name from chapter text
    public String extractChapterName(String chapterText) {

        String[] lines = chapterText.split("\\r?\\n");

        Pattern pattern = Pattern.compile(
                "(?i)^(chapter|unit|exercise|part|section|lesson|module)\\s+[A-Za-z0-9.IVXLC]+[:\\-.]?\\s*(.*)"
        );

        for (int i = 0; i < Math.min(10, lines.length); i++) {

            String line = lines[i].trim();

            if (line.isBlank()) continue;

            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                return line;
            }
        }

        // Fallback: return-first meaningful line
        for (int i = 0; i < Math.min(10, lines.length); i++) {
            if (!lines[i].trim().isBlank() && lines[i].length() > 5) {
                return lines[i].trim();
            }
        }

        return "Unknown Chapter";
    }


    // Find chapter by chapter id
    public Chapter findChapterById(long chapter_id){
        return chapterRepository.findById(chapter_id)
                .orElseThrow(()-> new NotFoundException("There is no chapter with id: "+chapter_id));
    }

    public ChapterDtoBasic saveChapter(ChapterDtoWithoutPath chapterDtoWithoutPath) {
        return chapterMapper.toChapterBasicDto(chapterRepository.save(chapterMapper.toChapter(chapterDtoWithoutPath)));
    }
}
