package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminsController {

    private final UserService userService;

    public AdminsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", ""})
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public String visitAdminPage(Principal principal, ModelMap model) {

        model.addAttribute("getUsers", userService.findAll()); //получаем всех пользователей

        String adminname = principal.getName();

        User admin = userService.findByUsername(adminname);

        model.addAttribute("admin", admin);

        return "admin";
    }

    @GetMapping("/404")
    public String showError(ModelMap model) {
        model.addAttribute("errorMessage", "Something went wrong");
        return "404";
    }

    @GetMapping("/newuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public String createUser(ModelMap model) {
        model.addAttribute("newUser", new User());
        return "new";
    }

    @PostMapping("/newuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public String newUser(@ModelAttribute("newUser") User user, ModelMap model) {

        Set<Role> roles = parseAndValidateRoles(user.getRoleNames());

        if (roles == null) {
            model.addAttribute("errorMessage", "Роль не найдена в базе.");
            return "404";
        }

        user.setRoles(roles);

        try {
            userService.save(user);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при сохранении пользователя: " + e.getMessage());
            return "404";
        }

        return "redirect:/admin";
    }

    @GetMapping("/deleteuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public String deleteUser(ModelMap model) {
        return "delete";
    }

    @PostMapping("/deleteuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public String removeUser(@RequestParam("id") long id, ModelMap model) {

        try {
            userService.delete(id);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Проблема с удалением пользователя: " + e.getMessage());
            return "404";
        }

        return "redirect:/admin";
    }

    @GetMapping("/edituser")
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public String changeUser(ModelMap model) {
        model.addAttribute("updateUser", new User());
        return "edit";
    }

    @PostMapping("/edituser")
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public String updateUser(@RequestParam("id") long id,
                             @ModelAttribute("updateUser") User user, ModelMap model) {

        String roleNames = user.getRoleNames();
        Set<Role> roles = parseAndValidateRoles(roleNames);

        if (roleNames != null && !roleNames.isBlank() && roles == null) {
            model.addAttribute("errorMessage", "Роль не найдена в БД");
            return "404";
        }

        user.setRoles(roles);

        try {
            userService.update(id, user);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Ошибка при изменении данных пользователя: " + e.getMessage());
            return "404";
        }

        return "redirect:/admin";
    }

    private Set<Role> parseAndValidateRoles(String roleNames) {

        if (roleNames == null || roleNames.isBlank()) {
            return null; // null и возвращаем
        }

        Set<Role> allRoles = userService.resolvedRoles(); // получаем все роли из БД
        String[] inputRoles = roleNames.split(","); //переводим в массив ролей
        Set<Role> resolvedRoles = new HashSet<>(); // создаем новый список присваиваемых ролей

        for (String inputRole : inputRoles) {
            String trimmed = inputRole.trim(); // убираем пробелы по краям
            Optional<Role> matchedRole = allRoles.stream() //проверяем наличие роли из массива в списке полученных ролей
                    .filter(r -> r.getRoleName().equalsIgnoreCase(trimmed))
                    .findFirst();

            if (matchedRole.isPresent()) {
                resolvedRoles.add(matchedRole.get());
            } else {
                return null; // если подходящих ролей нет, то возвращаем null
            }
        }

        return resolvedRoles;
    }

}
