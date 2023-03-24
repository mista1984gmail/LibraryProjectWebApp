package com.example.LibraryProjectWebApp.web.controller;

import com.example.LibraryProjectWebApp.persistance.entity.User;
import com.example.LibraryProjectWebApp.service.UserService;
import com.example.LibraryProjectWebApp.web.validator.EmailValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
@Slf4j
@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final EmailValidator emailValidator;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String registrationUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult emailBinding, BindingResult telephoneBinding,  Model model) {
        if (telephoneBinding.hasErrors()) {
            return "registration";
        }
        emailValidator.validate(userForm, emailBinding);
        log.info("Validate user by unique email");
        if (emailBinding.hasErrors()) {
            return "registration";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            model.addAttribute("passwordError", "Password mismatch");
            return "registration";
        }
        if (!userService.saveUser(userForm)){
            model.addAttribute("usernameError", "A user with the same name already exists");
            return "registration";
        }

        return "redirect:/";
    }
    @GetMapping("/activate/{code}")
    public String activateUser(Model model, @PathVariable String code){
        log.info("Activate user");
        boolean isActivated = userService.activateUser(code);
        if(isActivated){
            model.addAttribute("message","User successfully activated");
        }
        else{
            model.addAttribute("message","Activation code is not found");
        }
        return "registration_successfully";
    }

}
