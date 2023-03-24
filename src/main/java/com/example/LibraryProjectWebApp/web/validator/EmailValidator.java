package com.example.LibraryProjectWebApp.web.validator;

import com.example.LibraryProjectWebApp.persistance.entity.User;
import com.example.LibraryProjectWebApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class EmailValidator implements Validator {
    private final UserService userService;
    @Autowired
    public EmailValidator(UserService userService) {
        this.userService = userService;
    }
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User person = (User) o;

        if (userService.findByEmail(person.getEmail()).isPresent())
            errors.rejectValue("email", "", "A person with this email already exists");
    }
}
