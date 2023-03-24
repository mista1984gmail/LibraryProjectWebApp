package com.example.LibraryProjectWebApp.web.controller;


import com.example.LibraryProjectWebApp.persistance.entity.User;
import com.example.LibraryProjectWebApp.service.BookService;
import com.example.LibraryProjectWebApp.service.UserService;
import com.example.LibraryProjectWebApp.service.dto.BookDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final BookService bookService;

    @Operation(summary = "Main page")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String userPage(@PathVariable("id") Long id, Model model) {
        log.info("Find user by id: " + id);
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "/user/user";
    }

    @Operation(summary = "User info")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public String userInfo(@PathVariable("id") Long id, Model model) {
        log.info("Find user by id: " + id);
        User user = userService.findUserById(id);
        log.info("Find all books user with id: " + id);
        List<BookDto> listOfBooks = userService.getBooksByUserId(id);
        model.addAttribute("user", user);
        model.addAttribute("listOfBooks", listOfBooks);
        return "/user/user_info";
    }

    @Operation(summary = "Edit user form")
    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model){
        log.info("Find user by id: " + id);
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "/user/user_form";
    }

    @Operation(summary = "Save user")
    @PostMapping("/save/{id}")
    public String update(@PathVariable("id") Long id, @Valid User user){
        log.info("Update user by id: " + id);
        userService.update(user);
        return "redirect:/user/info/{id}";
    }
    @Operation(summary = "Show free books")
    @GetMapping("/books/{id}")
    public String index(@PathVariable("id") Long id, Model model, @ModelAttribute("person") User person) {
        log.info("Find user by id: " + id);
        model.addAttribute("person", userService.findUserById(id));
        log.info("Find free books");
        model.addAttribute("books", bookService.findByOwnerIsNullAndBookedIsFalse());
        return "user/books";
    }

    @Operation(summary = "Booking book")
    @GetMapping("/{userId}/book/add/{bookId}")
    public String assign(@PathVariable("userId") Long userId,@PathVariable("bookId") Long bookId) {
        log.info("Find user by id: " + userId);
        User selectedPerson = userService.findUserById(userId);
        log.info("Ordered book with id: " + bookId + " user: " + selectedPerson);
        bookService.ordered(bookId, selectedPerson);
        return "redirect:/user/books/" + userId;
    }
}
