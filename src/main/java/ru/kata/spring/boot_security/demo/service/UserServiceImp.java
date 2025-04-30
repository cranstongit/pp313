package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.dao.UserDAOImpl;
import ru.kata.spring.boot_security.demo.model.User;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService, UserDetailsService {

   private final UserDAO userDAO;
   private final UserDAOImpl userDAOImpl;

   public UserServiceImp(UserDAO userDAO, UserDAOImpl userDAOImpl) {
      this.userDAO = userDAO;
      this.userDAOImpl = userDAOImpl;
   }

   @Transactional
   @Override
   public void save(User user) {
      userDAO.save(user);
   }

   @Transactional
   @Override
   public void update(long id, User user) {
      User existingUser = userDAO.find(id);
      if (existingUser == null) {
         throw new EntityNotFoundException("User with id " + id + " not found");
      }

      existingUser.setFirstName(user.getFirstName());
      existingUser.setLastName(user.getLastName());
      existingUser.setEmail(user.getEmail());

      userDAO.save(existingUser);
   }

   @Transactional
   @Override
   public void delete(long id) {
      User existingUser = userDAO.find(id);
      if (existingUser == null) {
         throw new EntityNotFoundException("User with id " + id + " not found");
      }
      userDAO.delete(id);
   }

   @Override
   public List<User> findAll() {
      return userDAO.findAll();
   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//      Optional<User> user = userDAOImpl.findByUsername(String username);
//
//      if (user.isEmpty())
//         throw new UsernameNotFoundException("username не найден.");
//
//      return new user.get();
      return null;
   }
}
