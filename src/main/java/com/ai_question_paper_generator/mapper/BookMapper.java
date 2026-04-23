package com.ai_question_paper_generator.mapper;

import com.ai_question_paper_generator.dto.book_dto.BookDto;
import com.ai_question_paper_generator.dto.book_dto.BookDtoBasic;
import com.ai_question_paper_generator.dto.book_dto.BookDtoWithId;
import com.ai_question_paper_generator.dto.book_dto.BookDtoWithPath;
import com.ai_question_paper_generator.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book bookDtoWithPathToBook(BookDtoWithPath bookDtoWithPath);
    BookDtoBasic toBookDtoBasic(Book book);

    @Mapping(source = "id", target = "id")
    BookDtoWithId toBookDtoWithId(Book book);
    Book toBook(BookDtoWithId dto);

}
