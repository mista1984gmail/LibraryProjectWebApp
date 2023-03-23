package com.example.LibraryProjectWebApp.web.controller;

import com.example.LibraryProjectWebApp.persistance.entity.Role;
import com.example.LibraryProjectWebApp.persistance.entity.User;
import com.example.LibraryProjectWebApp.service.RoleService;
import com.example.LibraryProjectWebApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage() {
        return "/admin/admin";
    }
    @Operation(summary = "Find all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find journeys", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @RequestMapping(value = "/readers", method = RequestMethod.GET)
    public String showUsers(Model model) {
        List<User> users = userService.allUsers();
        model.addAttribute("users", users);
        return "/admin/admin_users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model){

        User user = userService.findUserById(id);
        List<Role>roles= roleService.rolesUserToList(user);
        List<Role>roleType= roleService.allRoles();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("roleType", roleType);
        return "admin/admin_user_form";
    }

    @PostMapping("/save/{id}")
    public String saveUser(@PathVariable("id") Long id, @Valid User user, HttpServletRequest request, BindingResult bindingResult){

        String[] detailIDs = request.getParameterValues("detailID");
        String[] detailNames = request.getParameterValues("detailName");
        userService.updateAdmin(user, detailIDs, detailNames);

        return "redirect:/admin/admin";
    }

}
