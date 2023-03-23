package com.example.LibraryProjectWebApp.web.controller;


import com.example.LibraryProjectWebApp.persistance.entity.Book;
import com.example.LibraryProjectWebApp.persistance.entity.User;
import com.example.LibraryProjectWebApp.service.BookService;
import com.example.LibraryProjectWebApp.service.UserService;
import com.example.LibraryProjectWebApp.service.dto.BookDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {


    private final UserService userService;
    private final BookService bookService;

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String userPage(@PathVariable("id") Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "/user/user";
    }

    @RequestMapping(value = "/history/{id}", method = RequestMethod.GET)
    public String userInfo(@PathVariable("id") Long id, Model model) {
        User user = userService.findUserById(id);
        List<BookDto> listOfBooks = userService.getBooksByUserId(id);

        model.addAttribute("user", user);
        model.addAttribute("listOfBooks", listOfBooks);
        return "/user/user_info";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model){
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "/user/user_form";
    }

    @PostMapping("/save/{id}")
    public String saveUser(@PathVariable("id") Long id, User user){
        userService.update(user);
        return "redirect:/user/history/{id}";
    }
    @GetMapping("/books/{id}")
    public String index(@PathVariable("id") Long id, Model model, @ModelAttribute("person") User person) {
        model.addAttribute("person", userService.findUserById(id));

        model.addAttribute("books", bookService.findByOwnerIsNullAndBookedIsFalse());

        return "user/books";
    }
    @GetMapping("/{userId}/book/add/{bookId}")
    public String assign(@PathVariable("userId") Long userId,@PathVariable("bookId") Long bookId) {
        User selectedPerson = userService.findUserById(userId);
        bookService.ordered(bookId, selectedPerson);
        return "redirect:/user/books/" + userId;
    }




}
