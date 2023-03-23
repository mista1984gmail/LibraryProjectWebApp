package com.example.LibraryProjectWebApp.service;


import com.example.LibraryProjectWebApp.persistance.entity.Role;
import com.example.LibraryProjectWebApp.persistance.entity.User;

import java.util.List;

public interface RoleService {
    List<Role> allRoles();

    List<Role> rolesUserToList(User user);
}
