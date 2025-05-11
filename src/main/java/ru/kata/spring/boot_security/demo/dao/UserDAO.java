package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserDAO {
    User findByUsername(String username);
    void save(User user);
    User find(long id);
    void delete(long id);
    List<User> findAll();
    Set<Role> resolvedRoles();
}