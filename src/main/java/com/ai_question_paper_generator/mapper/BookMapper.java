package com.ai_question_paper_generator.mapper;

import com.ai_question_paper_generator.dto.book_dto.BookDtoBasic;
import com.ai_question_paper_generator.dto.book_dto.BookDtoWithId;
import com.ai_question_paper_generator.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDtoBasic toBookDtoBasic(Book book);

    @Mapping(source = "id", target = "id")
    BookDtoWithId toBookDtoWithId(Book book);
    Book toBook(BookDtoWithId dto);

    Book toBookFromBookDtoBasic(BookDtoBasic bookDtoBasic);

    List<BookDtoWithId> toBookDtoWithIdList(List<Book> collect);
}
