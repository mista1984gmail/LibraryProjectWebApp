package com.example.LibraryProjectWebApp.service;

import com.example.LibraryProjectWebApp.persistance.entity.Book;
import com.example.LibraryProjectWebApp.persistance.entity.User;

import java.util.List;

public interface BookService {
    List<Book> findAll(boolean sortByYear);
    List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear);
    List<Book> findOverdueBooks();
    List<Book> findByOwnerIsNull();
    List<Book> findByOwnerIsNullAndBookedIsFalse();
    Book findOne(Long id);
    List<Book> searchByTitle(String query);
    void save(Book book);
    void update(Long id, Book updatedBook);
    void delete(Long id);
    User getBookOwner(Long id);
    void release(Long id);
    void assign(Long id, User selectedPerson);
    void ordered(Long id, User selectedPerson);

    void sendMessage(Long id, User user);
}
