package com.example.LibraryProjectWebApp.exception;

public class IdIsNotFoundOnDbException extends RuntimeException{
    public IdIsNotFoundOnDbException(Long id) {
        super("The id = '" + id + "' is not found on DB");}
}

