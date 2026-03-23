package com.ai_question_paper_generator.mapper;

import com.ai_question_paper_generator.dto.book_dto.BookDto;
import com.ai_question_paper_generator.dto.book_dto.BookDtoBasic;
import com.ai_question_paper_generator.dto.book_dto.BookDtoWithId;
import com.ai_question_paper_generator.dto.book_dto.BookDtoWithPath;
import com.ai_question_paper_generator.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ChapterMapper.class})
public interface BookMapper {
    Book dtoToBook(BookDto dto);
    BookDto bookToDto(Book book);
    Book basicBookDto(BookDtoBasic bookDtoBasic);
    Book bookDtoWithPathToBook(BookDtoWithPath bookDtoWithPath);
    BookDtoBasic toBookDtoBasic(Book book);
    BookDtoWithId toBookDtoWithId(Book book);
    Book toBook(BookDtoWithId dto);
}
