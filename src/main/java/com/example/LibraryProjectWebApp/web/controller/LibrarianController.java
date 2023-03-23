package com.example.LibraryProjectWebApp.web.controller;

import com.example.LibraryProjectWebApp.exception.IdIsNotFoundOnDbException;
import com.example.LibraryProjectWebApp.persistance.entity.Book;
import com.example.LibraryProjectWebApp.persistance.entity.User;
import com.example.LibraryProjectWebApp.service.BookService;
import com.example.LibraryProjectWebApp.service.UserService;
import com.example.LibraryProjectWebApp.service.convertor.BookMapper;
import com.example.LibraryProjectWebApp.service.dto.BookDto;
import lombok.AllArgsConstructor;
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

@Controller
@RequestMapping("/librarian")
@AllArgsConstructor
public class LibrarianController {

    private final UserService userService;
    private final BookService bookService;
    private final BookMapper bookMapper;


    @RequestMapping(value = "/librarian", method = RequestMethod.GET)
    public String librarianPage() {
        return "librarian/librarian";
    }

    @GetMapping("/book/new")
    public String createBookForm(@ModelAttribute("book") Book Book){
        return "librarian/book_form";
    }

    @PostMapping("/book")
    public String createBook(@ModelAttribute("book") @Valid BookDto Book,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "librarian/book_form";

        bookService.save(Book);
        return "redirect:/librarian/books";
    }

    @GetMapping("/book/show/librarian/book/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("book", bookService.findOne(id));
        return "librarian/edit_book";
    }

    @PostMapping("books/{id}")
    public String update(@ModelAttribute("book") @Valid BookDto book, BindingResult bindingResult,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors())
            return "librarian/edit_book";

        bookService.update(id, book);
        return "redirect:/librarian/books";
    }

    @PostMapping("/book/show/librarian/book/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        bookService.delete(id);
        return "redirect:/librarian/books";
    }

    @GetMapping("/book/{userId}/release/{bookId}")
    public String release(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId) {
        bookService.release(bookId);
        return "redirect:/librarian/books/{userId}";
    }

    @GetMapping("/book/show/{id}")
    public String show(@PathVariable("id") Long id, Model model, @ModelAttribute("person") User person) {
        model.addAttribute("book", bookService.findOne(id));

        User bookOwner = bookService.getBookOwner(id);

        if (bookOwner != null)
            model.addAttribute("owner", bookOwner);
        else
            model.addAttribute("people", userService.findAll());

        return "librarian/show_book";
    }

    @GetMapping("/book/show/librarian/books/{id}/assign")
    public String assign(@PathVariable("id") Long id, @ModelAttribute("person") User selectedPerson) {
        bookService.assign(id, selectedPerson);
        return "redirect:/librarian/book/show/" + id;
    }

    @GetMapping("/book/{userId}/assignBook/{bookId}")
    public String ordered(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId) {
        User user = userService.findUserById(userId);
        bookService.assign(bookId, user);
        return "redirect:/librarian/book/show/" + bookId;
    }


    @RequestMapping(value = "/readers", method = RequestMethod.GET)
    public String showReaders(Model model) {
        List<User> users = userService.allUsersWithRoleUser();
        model.addAttribute("users", users);
        return "librarian/readers";
    }

    @GetMapping("/books")
    public String showBooks(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {

        if (page == null || booksPerPage == null)
            model.addAttribute("books", bookService.findAll(sortByYear));
        else
            model.addAttribute("books", bookService.findWithPagination(page, booksPerPage, sortByYear));

        return "librarian/books";
    }

    @GetMapping("/books/{id}")
    public String userBooks(@PathVariable("id") Long id, Model model){
        User user = userService.findUserById(id);
        List<BookDto> books = bookMapper.toListDto(user.getBooks());
        model.addAttribute("user", user);
        model.addAttribute("books", books);

        return "librarian/books_user";
    }

    @GetMapping("/overdue-books")
    public String showOverdueBooks(Model model) {

            model.addAttribute("books", bookService.findOverdueBooks());

        return "librarian/overdue_books";
    }

    @GetMapping("/book/show/librarian/book/{id}/send-message")
    public String sendMessage(@PathVariable("id") Long id) {
        User user = bookService.getBookOwner(id);
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
