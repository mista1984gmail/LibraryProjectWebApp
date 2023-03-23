package com.example.LibraryProjectWebApp.service.convertor;

import com.example.LibraryProjectWebApp.persistance.entity.Book;
import com.example.LibraryProjectWebApp.service.dto.BookDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book dtoToModel(BookDto bookDto);

    BookDto modelToDto(Book book);

    List<BookDto> toListDto(List<Book> books);


}
