package com.example.LibraryProjectWebApp.service.impl;

import com.example.LibraryProjectWebApp.exception.IdIsNotFoundOnDbException;
import com.example.LibraryProjectWebApp.exception.UserNameNotFoundException;
import com.example.LibraryProjectWebApp.persistance.entity.Book;
import com.example.LibraryProjectWebApp.persistance.entity.Role;
import com.example.LibraryProjectWebApp.persistance.entity.User;
import com.example.LibraryProjectWebApp.persistance.repository.BooksRepository;
import com.example.LibraryProjectWebApp.persistance.repository.UserRepository;
import com.example.LibraryProjectWebApp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SendEmailService sendEmailService;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNameNotFoundException();
        }
        log.info("User "+ username + " found.");
        return user;
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> userFromDb= userRepository.findById(id);
        if(userFromDb==null){
            throw new IdIsNotFoundOnDbException(id);
        }

        log.info("User by id: " + id + " found.");
        return userFromDb.orElse(new User());
    }

    @Override
    public List<User> allUsers() {
        log.info("Find all users");
        return userRepository.findAll();
    }

    @Override
    public List<User> allUsersWithRoleUser() {
        List<User> users = userRepository.findAll();
        List<User> patientsList = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getAuthorities().toString().contains("ROLE_USER")) {
                patientsList.add(users.get(i));
            }
        }
        log.info("Find all users with role USER");
        return patientsList;
    }

    @Override
    @Transactional
    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB != null) {
            log.info("User " + user.getFirstName() +", " + user.getLastName() + " (" + user.getId() + ")"
                    +  " is exists.");
            return false;
        }
        user.setRoles(Collections.singleton(new Role(1l, "ROLE_USER")));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(false);
        userRepository.save(user);

        log.info("User " + user.getFirstName() +", " + user.getLastName() + " (" + user.getId() + ")"
                +  " saved.");

        if(!StringUtils.isEmpty(user.getEmail())){
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Library. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getFirstName(),
                    user.getActivationCode()
            );
            sendEmailService.send(user.getEmail(),"Activation code", message);
            log.info("User " + user.getFirstName() +", " + user.getLastName()
                    + " (" + user.getId() + ")" +  " an activation code has been sent.");
        }
        return true;
    }

    @Override
    @Transactional
    public void update(User user) {
        user.setActive(true);
        user.setRoles(Collections.singleton(new Role(1l, "ROLE_USER")));
        userRepository.save(user);
        log.info("User " + user.getFirstName() +", " + user.getLastName()
                + " (" + user.getId() + ")" +  " updated.");
    }

    @Override
    @Transactional
    public void updateAdmin(User user, String[] detailIDs, String[] detailNames) {

        for(int i = 0; i < detailNames.length; i++){
            if(detailIDs != null && detailIDs.length > 0){
                String role = detailNames[i];
                switch (role){
                    case "ROLE_USER":
                        user.setRoles(Collections.singleton(new Role(1L, detailNames[i])));
                        break;
                    case "ROLE_ADMIN":
                        user.setRoles(Collections.singleton(new Role(2L, detailNames[i])));
                        break;
                    case "ROLE_LIBRARIAN":
                        user.setRoles(Collections.singleton(new Role(3L, detailNames[i])));
                        break;
                }
            }

        }
        userRepository.save(user);

        log.info("User " + user.getFirstName() +", " + user.getLastName()
                + " (" + user.getId() + ")" +  " updated.");
    }


    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNameNotFoundException();
        }
        log.info("User "+ username +  " found.");
        return user;
    }


    @Override
    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null){
            return false;
        }
        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);

        log.info("User " + user.getFirstName() +", " + user.getLastName()
                + " (" + user.getId() + ")" + " activated.");
        return true;
    }
    public List<Book> getBooksByUserId(Long id) {
        Optional<User> person = userRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            person.get().getBooks().forEach(book -> {
                long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                // 864000000 милисекунд = 10 суток
                if (diffInMillies > 1800000)
                    book.setExpired(true);
            });

            return person.get().getBooks();
        }
        else {
            return Collections.emptyList();
        }
    }
}
