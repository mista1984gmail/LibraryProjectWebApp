package com.example.LibraryProjectWebApp.service.impl;


import com.example.LibraryProjectWebApp.config.Constant;
import com.example.LibraryProjectWebApp.persistance.entity.Book;
import com.example.LibraryProjectWebApp.persistance.entity.User;
import com.example.LibraryProjectWebApp.persistance.repository.BooksRepository;
import com.example.LibraryProjectWebApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BooksServiceImpl implements BookService {

    private final BooksRepository booksRepository;
    private final SendEmailService sendEmailService;

    @Autowired
    public BooksServiceImpl(BooksRepository booksRepository, SendEmailService sendEmailService) {
        this.booksRepository = booksRepository;
        this.sendEmailService = sendEmailService;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        else
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    @Override
    public List<Book> findOverdueBooks() {
        return booksRepository.findAll().stream()
                .filter(e -> e.getTakenAt()!=null)
                .filter(e -> Math.abs(e.getTakenAt().getTime() - new Date().getTime()) > Constant.BOOK_RETURN_DEADLINE)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByOwnerIsNull() {
        return booksRepository.findByOwnerIsNull();
    }

    @Override
    public List<Book> findByOwnerIsNullAndBookedIsFalse() {
        return booksRepository.findByOwnerIsNullAndBookedIsFalse();
    }

    public Book findOne(Long id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    public List<Book> searchByTitle(String query) {
        return booksRepository.findByTitleStartingWith(query);
    }

    @Transactional
    public void save(Book book) {
        book.setCodeBook(UUID.randomUUID().toString());
        book.setTakenAt(new Date());
        book.setBooked(false);
        booksRepository.save(book);
    }

    @Transactional
    public void update(Long id, Book updatedBook) {
        Book bookToBeUpdated = booksRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setTakenAt(new Date());
        updatedBook.setOwner(bookToBeUpdated.getOwner());
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(Long id) {
        booksRepository.deleteById(id);
    }

    public User getBookOwner(Long id) {
        return booksRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void release(Long id) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setTakenAt(null);
                    book.setBooked(false);
                });
    }

    @Transactional
    public void assign(Long id, User selectedPerson) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(selectedPerson);
                    book.setTakenAt(new Date());
                    book.setBooked(false);
                }
        );
    }

    @Override
    @Transactional
    public void ordered(Long id, User selectedPerson) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(selectedPerson);
                    book.setTakenAt(new Date());
                    book.setBooked(true);
                }
        );
    }

    @Override
    public void sendMessage(Long id, User user) {
        Book book = booksRepository.findById(id).get();

        sendEmailService.sendEmail(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                book.getTitle() + ", " + book.getAuthor() + ",  " + book.getYear(),
                book.getReturnBefore().toString());
    }
}
