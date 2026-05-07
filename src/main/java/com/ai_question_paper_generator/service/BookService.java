package com.ai_question_paper_generator.service;

import com.ai_question_paper_generator.dto.book_dto.BookDtoBasic;
import com.ai_question_paper_generator.dto.book_dto.BookDtoWithId;
import com.ai_question_paper_generator.exception.FileNotSavedException;
import com.ai_question_paper_generator.exception.NotFoundException;
import com.ai_question_paper_generator.mapper.BookMapper;
import com.ai_question_paper_generator.model.Book;
import com.ai_question_paper_generator.model.User;
import com.ai_question_paper_generator.repository.BookRepository;
import com.ai_question_paper_generator.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UserRepository userRepository;

    public BookDtoWithId saveBook(BookDtoBasic bookDtoBasic){

        try{
            User user = userRepository.findById(bookDtoBasic.getUserId())
                    .orElseThrow(()->new NotFoundException("user not found"));
            Book book = bookMapper.toBookFromBookDtoBasic(bookDtoBasic);
            book.setUser(user);
            return bookMapper.toBookDtoWithId(bookRepository.save(book));
        } catch (Exception e) {
            throw new FileNotSavedException("file could not saved.");
        }
    }

    public List<BookDtoWithId> findBookByUserEmail(String email){
        return bookMapper.toBookDtoWithIdList(bookRepository.findByUserEmail(email)
                .stream()
                .collect(Collectors.toList())
        );
    }

}
