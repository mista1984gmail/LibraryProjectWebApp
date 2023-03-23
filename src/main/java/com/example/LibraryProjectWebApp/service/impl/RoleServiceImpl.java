package com.example.LibraryProjectWebApp.service.impl;

import com.example.LibraryProjectWebApp.persistance.entity.Role;
import com.example.LibraryProjectWebApp.persistance.entity.User;
import com.example.LibraryProjectWebApp.persistance.repository.RoleRepository;
import com.example.LibraryProjectWebApp.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> allRoles() {
        log.info("Find all roles");
        return roleRepository.findAll();
    }

    @Override
    public List<Role> rolesUserToList(User user) {
        Set<Role> listRoles = new HashSet<>();
        listRoles=user.getRoles();
        List<Role>roles= new ArrayList<>();
        Iterator<Role> iterator = listRoles.iterator();
        while (iterator.hasNext()) {
            Role role = iterator.next();
            roles.add(role);
        }
        return roles;
    }
}
