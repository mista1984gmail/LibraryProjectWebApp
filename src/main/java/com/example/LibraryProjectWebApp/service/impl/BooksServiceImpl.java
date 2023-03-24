package com.example.LibraryProjectWebApp.service.impl;


import com.example.LibraryProjectWebApp.config.Constant;
import com.example.LibraryProjectWebApp.persistance.entity.Book;
import com.example.LibraryProjectWebApp.persistance.entity.User;
import com.example.LibraryProjectWebApp.persistance.repository.BooksRepository;
import com.example.LibraryProjectWebApp.service.BookService;
import com.example.LibraryProjectWebApp.service.convertor.BookMapper;
import com.example.LibraryProjectWebApp.service.dto.BookDto;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@Transactional(readOnly = true)
public class BooksServiceImpl implements BookService {

    private final BooksRepository booksRepository;
    private final SendEmailService sendEmailService;
    private final BookMapper bookMapper;

    @Autowired
    public BooksServiceImpl(BooksRepository booksRepository, SendEmailService sendEmailService, BookMapper bookMapper) {
        this.booksRepository = booksRepository;
        this.sendEmailService = sendEmailService;
        this.bookMapper = bookMapper;
    }

    public List<BookDto> findAll() {
            log.info("Find all books");
            return bookMapper.toListDto(booksRepository.findAll());
    }

    public List<BookDto> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear){
            log.info("Find all books and sort by year");
            return bookMapper.toListDto(booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent());}
        else{
            log.info("Find all books");
            return bookMapper.toListDto(booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent());}
    }

    @Override
    public List<BookDto> findOverdueBooks() {
        log.info("Find all overdue books");
        return booksRepository.findAll().stream()
                .filter(e -> e.getTakenAt() != null)
                .filter(e -> Math.abs(e.getTakenAt().getTime() - new Date().getTime()) > Constant.BOOK_RETURN_DEADLINE_MS).toList()
                .stream()
                .map(bookMapper::modelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> findByOwnerIsNull() {
        log.info("Find all books if owner is null");
        return bookMapper.toListDto(booksRepository.findByOwnerIsNull());
    }

    @Override
    public List<BookDto> findByOwnerIsNullAndBookedIsFalse() {
        log.info("Find all books if owner is null and booked is false");
        return bookMapper.toListDto(booksRepository.findByOwnerIsNullAndBookedIsFalse());
    }

    public BookDto findOne(Long id) {
        log.info("Find book by id: " + id);
        Optional<Book> foundBook = booksRepository.findById(id);
        return bookMapper.modelToDto(foundBook.orElse(null));
    }

    public List<BookDto> searchByTitle(String query) {
        log.info("Find book by title: " + query);
        return bookMapper.toListDto(booksRepository.findByTitleStartingWith(query));
    }

    @Transactional
    public void save(BookDto book) {
        book.setCodeBook(UUID.randomUUID().toString());
        book.setTakenAt(null);
        book.setBooked(false);
        log.info("Save book with code: " + book.getCodeBook());
        booksRepository.save(bookMapper.dtoToModel(book));
    }

    @Transactional
    public void update(Long id, BookDto updatedBook) {
        log.info("Find book by id: " + id);
        Book bookToBeUpdated = booksRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setTakenAt(null);
        updatedBook.setOwner(bookToBeUpdated.getOwner());

        log.info("Update book with code: " + updatedBook.getCodeBook());
        booksRepository.save(bookMapper.dtoToModel(updatedBook));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Delete book by id: " + id);
        booksRepository.deleteById(id);
    }

    public User getBookOwner(Long id) {
        log.info("Find user by book id: " + id);
        return booksRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void release(Long id) {
        log.info("Release book id: " + id);
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setTakenAt(null);
                    book.setBooked(false);
                });
    }

    @Transactional
    public void assign(Long id, User selectedPerson) {
        log.info("Assign book id: " + id + ", by user id: " + selectedPerson.getId());
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
        log.info("Ordered book id: " + id + ", by user id: " + selectedPerson.getId());
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
        log.info("Find book by id: " + id);
        BookDto book = bookMapper.modelToDto(booksRepository.findById(id).get());
        log.info("Send email by user id: " + user.getId() + ", about returning the book.");
        sendEmailService.sendEmail(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                book.getTitle() + ", " + book.getAuthor() + ",  " + book.getYear(),
                book.getReturnBefore().toString());
    }
}
