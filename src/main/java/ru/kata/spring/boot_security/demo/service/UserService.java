package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {
    void save(User user);
    void update(long id, User user);
    void delete(long id);
    List<User> findAll();
    User findByUsername(String username);
    Set<Role> resolvedRoles();
}