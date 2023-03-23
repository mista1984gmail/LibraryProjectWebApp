package com.example.LibraryProjectWebApp.exception;

public class UserNameNotFoundException extends RuntimeException {
    public UserNameNotFoundException() {
        super("User not found");}
}
