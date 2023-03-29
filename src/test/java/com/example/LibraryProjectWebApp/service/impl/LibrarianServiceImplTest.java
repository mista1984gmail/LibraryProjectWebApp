package com.example.LibraryProjectWebApp.service.impl;

import com.example.LibraryProjectWebApp.persistance.entity.Book;
import com.example.LibraryProjectWebApp.persistance.repository.BooksRepository;
import com.example.LibraryProjectWebApp.service.convertor.BookMapper;
import com.example.LibraryProjectWebApp.service.dto.BookDto;
import com.example.LibraryProjectWebApp.util.BookFixture;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibrarianServiceImplTest {
    public static final Long ID = 1L;

    @Mock
    private BooksRepository repository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    BooksServiceImpl bookService;

    @Test
    void findAll() {
        //given
        BookDto bookFirst = BookFixture.createFirstBook();
        BookDto bookSecond = BookFixture.createSecondBook();

        var books = List.of(bookFirst, bookSecond);

        doReturn(books)
                .when(repository)
                .findAll();

        //when
        repository.save(bookMapper.dtoToModel(bookFirst));
        repository.save(bookMapper.dtoToModel(bookSecond));
        List<Book> booksFromDb = repository.findAll();

        //then
        Assertions.assertTrue(CoreMatchers.is(booksFromDb.size()).matches(2));
    }

    @Test
    void save() {
        //given
        BookDto bookDto = BookFixture.createFirstBook();

        //when
        repository.save(bookMapper.dtoToModel(bookDto));

        //then
        Assertions.assertNotNull(bookDto.getId());
        Assertions.assertNotNull(bookDto.getTitle());
        Assertions.assertNotNull(bookDto.getAuthor());
        Assertions.assertNotNull(bookDto.getYear());
        Assertions.assertEquals("Head First Java",bookDto.getTitle());
        Assertions.assertEquals("Kathy Sierra & Bert Bates",bookDto.getAuthor());
        Assertions.assertEquals(2012,bookDto.getYear());
        verify(repository, times(1)).save(bookMapper.dtoToModel(bookDto));
    }

    @Test
    void delete() {
        //given
        BookDto bookDto = BookFixture.createFirstBook();

        //when
        repository.save(bookMapper.dtoToModel(bookDto));

        bookService.delete(ID);

        //then
        verify(repository, times(1)).deleteById(any());
    }

    @Test
    void findOne() {
        //given
        BookDto bookDto = BookFixture.createFirstBook();
        Book book = bookMapper.dtoToModel(bookDto);

        //when
        when(repository.findById(ID)).thenReturn(Optional.ofNullable(book));
        when(bookMapper.modelToDto(book)).thenReturn(bookDto);

        var actual = bookService.findOne(ID);

        //then
        verify(repository).findById(ID);
        Assertions.assertEquals(bookDto, actual);
    }
    @Test
    void update() {
        //given

        Book existBook = BookFixture.existBook();
        BookDto existBookDto = bookMapper.modelToDto(existBook);
        Book savedBook = BookFixture.savedBook();
        BookDto savedBookDto = BookFixture.createFirstBook();

        //when
        when(repository.findById(ID)).thenReturn(Optional.ofNullable(existBook));
        when(repository.save(any())).thenReturn(savedBook);
        when(bookMapper.dtoToModel(savedBookDto)).thenReturn(savedBook);

        bookService.update(ID, savedBookDto);


        when(repository.findById(ID)).thenReturn(Optional.ofNullable(savedBook));
        when(bookMapper.modelToDto(savedBook)).thenReturn(savedBookDto);

        var actual = bookService.findOne(ID);

        //then
        Assertions.assertNotNull(savedBookDto.getId());
        Assertions.assertNotNull(savedBookDto.getTitle());
        Assertions.assertNotNull(savedBookDto.getAuthor());
        Assertions.assertNotNull(savedBookDto.getYear());

        Assertions.assertEquals(savedBookDto,actual);
        verify(repository, times(1)).save(bookMapper.dtoToModel(savedBookDto));
    }

}
