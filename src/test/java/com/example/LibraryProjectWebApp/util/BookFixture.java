package com.example.LibraryProjectWebApp.util;

import com.example.LibraryProjectWebApp.persistance.entity.Book;
import com.example.LibraryProjectWebApp.service.dto.BookDto;

public class BookFixture {
    private static final Long ID_FIRST = 1L;
    private static final String TITLE_FIRST = "Head First Java";
    private static final String AUTHOR_FIRST = "Kathy Sierra & Bert Bates";
    private static final int YEAR_FIRST = 2012;
    private static final Long ID_SECOND = 2L;
    private static final String TITLE_SECOND = "Java: A Beginners Guide";
    private static final String AUTHOR_SECOND = "Herbert Schildt";
    private static final int YEAR_SECOND = 2015;


    public static BookDto createFirstBook(){
        return new BookDto(ID_FIRST, TITLE_FIRST, AUTHOR_FIRST, YEAR_FIRST);
    }
    public static BookDto createSecondBook(){
        return new BookDto(ID_SECOND, TITLE_SECOND, AUTHOR_SECOND, YEAR_SECOND);
    }

    public static Book existBook(){
        return new Book(ID_FIRST, TITLE_SECOND, AUTHOR_FIRST, YEAR_FIRST);
    }
    public static Book savedBook(){
        return new Book(ID_FIRST, TITLE_FIRST, AUTHOR_FIRST, YEAR_FIRST);
    }

}
