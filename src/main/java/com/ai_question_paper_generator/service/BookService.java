package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.book_dto.BookDtoBasic;
import com.ai_question_paper_generator.dto.book_dto.BookDtoWithId;
import com.ai_question_paper_generator.exception.FileNotSavedException;
import com.ai_question_paper_generator.exception.NotFoundException;
import com.ai_question_paper_generator.mapper.BookMapper;
import com.ai_question_paper_generator.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookDtoWithId saveBook(BookDtoBasic bookDtoBasic){

        try{
            return bookMapper.toBookDtoWithId(bookRepository.save(bookMapper.toBookFromBookDtoBasic(bookDtoBasic)));
        } catch (Exception e) {
            throw new FileNotSavedException("file could not saved.");
        }
    }

    public BookDtoBasic findBookById(long book_id){
        return bookMapper.toBookDtoBasic(bookRepository.findById(book_id)
                .orElseThrow(()-> new NotFoundException("there is no book.")));
    }

}
