package com.example.LibraryProjectWebApp.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IdIsNotFoundOnDbException.class)
    public String handleIdNotFoundException(IdIsNotFoundOnDbException ex, Model model) {
        log.info("handleIdNotFoundException - Catching: " + ex.getClass().getSimpleName());
        model.addAttribute("messages","Something went wrong. Contact technical support.");
        return "exception_page";
    }

    @ExceptionHandler(UserNameNotFoundException.class)
    public String handleUserNameNotFoundException(UserNameNotFoundException ex, Model model) {
        log.info("handleUserNameNotFoundException - Catching: " + ex.getClass().getSimpleName());
        model.addAttribute("messages","Something went wrong. Contact technical support.");
        return "exception_page";
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, Model model) {
        log.info("handleHttpRequestMethodNotSupportedException - Catching: " + ex.getClass().getSimpleName());
        model.addAttribute("messages","Invalid data, please try again.");
        return "exception_page";
    }


}
