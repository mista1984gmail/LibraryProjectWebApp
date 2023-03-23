package com.example.LibraryProjectWebApp.persistance.repository;


import com.example.LibraryProjectWebApp.persistance.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleStartingWith(String title);
    List<Book> findByOwnerIsNull();
    List<Book> findByOwnerIsNullAndBookedIsFalse();
}
