package com.example.LibraryProjectWebApp.service;


import com.example.LibraryProjectWebApp.persistance.entity.User;
import com.example.LibraryProjectWebApp.service.dto.BookDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    User findUserById(Long id);

    List<User> findAll();

    List<User> allUsersWithRoleUser();

    boolean saveUser(User appUser);

    void update(User user);

    void updateAdmin(User user, String[] detailIDs, String[] detailNames);

    User findByUsername(String username);

    boolean activateUser(String code);
    List<BookDto> getBooksByUserId(Long id);
    Optional<User> findByEmail(String email);


}
