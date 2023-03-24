package com.example.LibraryProjectWebApp.web.controller;

import com.example.LibraryProjectWebApp.exception.IdIsNotFoundOnDbException;
import com.example.LibraryProjectWebApp.persistance.entity.Book;
import com.example.LibraryProjectWebApp.persistance.entity.User;
import com.example.LibraryProjectWebApp.service.BookService;
import com.example.LibraryProjectWebApp.service.UserService;
import com.example.LibraryProjectWebApp.service.convertor.BookMapper;
import com.example.LibraryProjectWebApp.service.dto.BookDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Slf4j
@Controller
@RequestMapping("/librarian")
@AllArgsConstructor
public class LibrarianController {

    private final UserService userService;
    private final BookService bookService;
    private final BookMapper bookMapper;

    @Operation(summary = "Main page")
    @RequestMapping(value = "/librarian", method = RequestMethod.GET)
    public String librarianPage() {
        return "librarian/librarian";
    }

    @Operation(summary = "Create book")
    @GetMapping("/book/new")
    public String createBookForm(@ModelAttribute("book") Book Book){
        return "librarian/book_form";
    }
    @Operation(summary = "Create book")
    @PostMapping("/book")
    public String create(@ModelAttribute("book") @Valid BookDto book,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "librarian/book_form";
        bookService.save(book);
        log.info("Save book: " + book);
        return "redirect:/librarian/books";
    }

    @Operation(summary = "Edit book")
    @GetMapping("/book/show/librarian/book/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        log.info("Find book by id: " + id);
        model.addAttribute("book", bookService.findOne(id));
        return "librarian/edit_book";
    }

    @Operation(summary = "Update book")
    @PostMapping("books/{id}")
    public String update(@ModelAttribute("book") @Valid BookDto book, BindingResult bindingResult,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors())
            return "librarian/edit_book";

        bookService.update(id, book);
        log.info("Update book: " + book);
        return "redirect:/librarian/books";
    }

    @Operation(summary = "Delete book")
    @PostMapping("/book/show/librarian/book/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        bookService.delete(id);
        log.info("Delete book by id: " + id);
        return "redirect:/librarian/books";
    }

    @Operation(summary = "Release book")
    @GetMapping("/book/{userId}/release/{bookId}")
    public String release(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId) {
        bookService.release(bookId);
        log.info("Release book by id: " + bookId + " from user with id: " + userId);
        return "redirect:/librarian/books/{userId}";
    }

    @Operation(summary = "Show book")
    @GetMapping("/book/show/{id}")
    public String show(@PathVariable("id") Long id, Model model, @ModelAttribute("person") User person) {
        log.info("Find book by id: " + id);
        model.addAttribute("book", bookService.findOne(id));
        log.info("Find user by book id: " + id);
        User bookOwner = bookService.getBookOwner(id);

        if (bookOwner != null)
            model.addAttribute("owner", bookOwner);
        else
            model.addAttribute("people", userService.findAll());

        return "librarian/show_book";
    }

    @Operation(summary = "Assign book")
    @GetMapping("/book/show/librarian/books/{id}/assign")
    public String assign(@PathVariable("id") Long id, @ModelAttribute("person") User selectedPerson) {
        bookService.assign(id, selectedPerson);
        log.info("Assign user: " + selectedPerson + " book with id: " + id);
        return "redirect:/librarian/book/show/" + id;
    }

    @Operation(summary = "Order book")
    @GetMapping("/book/{userId}/assignBook/{bookId}")
    public String ordered(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId) {
        log.info("Find user by id: " + userId);
        User user = userService.findUserById(userId);
        log.info("Assign user: " + user + " book with id: " + bookId);
        bookService.assign(bookId, user);
        return "redirect:/librarian/book/show/" + bookId;
    }

    @Operation(summary = "Show readers")
    @RequestMapping(value = "/readers", method = RequestMethod.GET)
    public String showReaders(Model model) {
        log.info("Find all users with role USER");
        List<User> users = userService.allUsersWithRoleUser();
        model.addAttribute("users", users);
        return "librarian/readers";
    }

    @Operation(summary = "Show books")
    @GetMapping("/books")
    public String showBooks(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {
        log.info("Show all books");
        if (page == null || booksPerPage == null)
            model.addAttribute("books", bookService.findAll());
        else
            model.addAttribute("books", bookService.findWithPagination(page, booksPerPage, sortByYear));
        return "librarian/books";
    }

    @Operation(summary = "Show user books")
    @GetMapping("/books/{id}")
    public String userBooks(@PathVariable("id") Long id, Model model){
        log.info("Find user by id: " + id);
        User user = userService.findUserById(id);
        log.info("Find all books user: " + user);
        List<BookDto> books = bookMapper.toListDto(user.getBooks());
        model.addAttribute("user", user);
        model.addAttribute("books", books);
        return "librarian/books_user";
    }
    @Operation(summary = "Find overdue books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find clients", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/overdue-books")
    public String showOverdueBooks(Model model) {
        log.info("Find all overdue books");
        model.addAttribute("books", bookService.findOverdueBooks());

        return "librarian/overdue_books";
    }

    @Operation(summary = "Send email message")
    @GetMapping("/book/show/librarian/book/{id}/send-message")
    public String sendMessage(@PathVariable("id") Long id) {
        log.info("Find user by book id: " + id);
        User user = bookService.getBookOwner(id);
        log.info("Send user: " + user + " email");
        bookService.sendMessage(id, user);
        return "redirect:/librarian/books";
    }


    @ExceptionHandler(value = IdIsNotFoundOnDbException.class)
    public String exceptionHandlerNull(Model model){
        model.addAttribute("msg","Null Pointer exception has occured");
        return "null_page";
    }

    @InitBinder
    public void bindingPreparation(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor orderDateEditor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, orderDateEditor);
    }
}
