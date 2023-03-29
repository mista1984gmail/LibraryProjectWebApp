package com.example.LibraryProjectWebApp.service;

import com.example.LibraryProjectWebApp.config.Constant;
import com.example.LibraryProjectWebApp.service.dto.BookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final BookService bookService;

    @Scheduled(initialDelayString = Constant.INIT_DELAY_SCHEDULE, fixedDelayString = Constant.BOOKING_CHECK_SCHEDULE)
    public void checkBookingTimeHasBeenExceeded() throws InterruptedException {
        log.info("Find all books");
        List<BookDto> books = bookService.findAll();
        log.info("On schedule: check booking time has been exceeded");
        books.stream()
                .filter(BookDto::isBooked)
                .filter(book -> Math.abs(book.getTakenAt().getTime() - new Date().getTime()) > Constant.BOOK_TAKE_DEADLINE_MS).toList()
                .forEach(book -> bookService.release(book.getId()));
    }
}
