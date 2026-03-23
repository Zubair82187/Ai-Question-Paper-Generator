package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.book_dto.BookDtoBasic;
import com.ai_question_paper_generator.dto.book_dto.BookDtoWithId;
import com.ai_question_paper_generator.dto.book_dto.BookDtoWithPath;
import com.ai_question_paper_generator.exception.NotFoundException;
import com.ai_question_paper_generator.mapper.BookMapper;
import com.ai_question_paper_generator.model.Book;
import com.ai_question_paper_generator.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookDtoWithId saveBook(BookDtoWithPath bookDtoWithPath){
        return bookMapper.toBookDtoWithId(bookRepository.save(bookMapper.bookDtoWithPathToBook(bookDtoWithPath)));
    }

    public BookDtoBasic findBookById(long book_id){
        return bookMapper.toBookDtoBasic(bookRepository.findById(book_id)
                .orElseThrow(()-> new NotFoundException("there is no book.")));
    }

    public BookDtoBasic saveBook(Book book) {
        return null;
    }
}
