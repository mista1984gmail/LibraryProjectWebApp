package com.example.LibraryProjectWebApp.persistance.repository;

import com.example.LibraryProjectWebApp.persistance.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
