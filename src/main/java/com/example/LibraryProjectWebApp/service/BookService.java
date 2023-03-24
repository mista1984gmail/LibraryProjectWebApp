package com.example.LibraryProjectWebApp.service;

import com.example.LibraryProjectWebApp.persistance.entity.User;
import com.example.LibraryProjectWebApp.service.dto.BookDto;

import java.util.List;

public interface BookService {
    List<BookDto> findAll();
    List<BookDto> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear);
    List<BookDto> findOverdueBooks();
    List<BookDto> findByOwnerIsNull();
    List<BookDto> findByOwnerIsNullAndBookedIsFalse();
    BookDto findOne(Long id);
    List<BookDto> searchByTitle(String query);
    void save(BookDto bookDto);
    void update(Long id, BookDto updatedBook);
    void delete(Long id);
    User getBookOwner(Long id);
    void release(Long id);
    void assign(Long id, User selectedPerson);
    void ordered(Long id, User selectedPerson);

    void sendMessage(Long id, User user);
}
