package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

   private final RoleDao roleDao;

   public RoleServiceImpl(RoleDao roleDao) {
      this.roleDao = roleDao;
   }

//   @Override
//   public Set<Role> resolvedRoles() {
//      return roleDao.resolvedRoles();
//   }
//
//   @Override
//   public Set<Role> parseAndValidateRoles(String roleNames) {
//
//      if (roleNames == null || roleNames.isBlank()) {
//         return null; // null и возвращаем
//      }
//
//      Set<Role> allRoles = roleDao.resolvedRoles(); // получаем все роли из БД
//      String[] inputRoles = roleNames.split(","); //переводим в массив ролей
//      Set<Role> resolvedRoles = new HashSet<>(); // создаем новый список присваиваемых ролей
//
//      for (String inputRole : inputRoles) {
//         String trimmed = inputRole.trim(); // убираем пробелы по краям
//         Optional<Role> matchedRole = allRoles.stream() //проверяем наличие роли из массива в списке полученных ролей
//                 .filter(r -> r.getRoleName().equalsIgnoreCase(trimmed))
//                 .findFirst();
//
//         if (matchedRole.isPresent()) {
//            resolvedRoles.add(matchedRole.get());
//         } else {
//            return null; // если подходящих ролей нет, то возвращаем null
//         }
//      }
//
//      return resolvedRoles;
//   }

   public Set<Role> findAll() {
      return roleDao.findAll();
   }

   public Set<Role> findByIds(List<Long> ids) {
      return roleDao.findByIds(ids);
   }

}
