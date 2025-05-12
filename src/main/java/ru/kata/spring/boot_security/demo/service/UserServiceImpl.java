package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

   private final UserDao userDao;
   private final PasswordEncoder passwordEncoder;

   public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
      this.userDao = userDao;
      this.passwordEncoder = passwordEncoder;
   }

   @Transactional
   @Override
   public void save(User user) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userDao.save(user);
   }

   @Transactional
   @Override
   public void update(long id, User user) {

      User existingUser = userDao.find(id);

      if (existingUser == null) {
         throw new EntityNotFoundException("User with id " + id + " not found");
      }

      if (user.getFirstName() != null && !user.getFirstName().isBlank()) {
         existingUser.setFirstName(user.getFirstName());
      }

      if (user.getLastName() != null && !user.getLastName().isBlank()) {
         existingUser.setLastName(user.getLastName());
      }

      if (user.getEmail() != null && !user.getEmail().isBlank()) {
         existingUser.setEmail(user.getEmail());
      }

      if (user.getUsername() != null && !user.getUsername().isBlank()) {
         existingUser.setUsername(user.getUsername());
      }

      if (user.getPassword() != null && !user.getPassword().isBlank()) {
         existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
      }

      if (user.getRoles() != null && !user.getRoles().isEmpty()) {
         existingUser.setRoles(user.getRoles());
      }

      userDao.save(existingUser);
   }

   @Transactional
   @Override
   public void delete(long id) {

      User existingUser = userDao.find(id);

      if (existingUser == null) {
         throw new EntityNotFoundException("User with id " + id + " not found");
      }

      userDao.delete(id);
   }

   @Override
   public List<User> findAll() {
      return userDao.findAll();
   }

   @Override
   public User findByUsername(String username) {
      return userDao.findByUsername(username);
   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      User foundUser = userDao.findByUsername(username);

      if (foundUser == null)
         throw new UsernameNotFoundException(username + " not found");

      return new org.springframework.security.core.userdetails.User(foundUser.getUsername(),
                                                                     foundUser.getPassword(),
                                                                     mapRolesToAuthorities(foundUser.getRoles()));
   }

   private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
      return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
   }

}
