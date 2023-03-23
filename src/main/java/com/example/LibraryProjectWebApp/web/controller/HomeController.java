package com.example.LibraryProjectWebApp.web.controller;

import com.example.LibraryProjectWebApp.persistance.entity.User;
import com.example.LibraryProjectWebApp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@AllArgsConstructor
public class HomeController {

    private final UserService userService;



    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/success")
    public void loginPageRedirect(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {

        String role =  authResult.getAuthorities().toString();
        String name =authResult.getName();
        User user = userService.findByUsername(name);
        Long id = user.getId();


        if(user.isActive()) {

            if (role.contains("ROLE_ADMIN")) {
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/admin/admin"));
            } else if (role.contains("ROLE_USER")) {
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/user/user/" + id));
            } else if (role.contains("ROLE_LIBRARIAN")) {
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/librarian/librarian"));
            }
        }
        else {
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/404"));
        }

    }

    @RequestMapping(value = "/limited_access", method = RequestMethod.GET)
    public String accessDenied() {
        return "limited_access";
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String accessActivation() {
        return "404";
    }

}
