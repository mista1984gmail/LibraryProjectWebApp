package com.example.LibraryProjectWebApp.persistance.repository;

import com.example.LibraryProjectWebApp.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);
    Optional<User> findByEmail(String email);
}
