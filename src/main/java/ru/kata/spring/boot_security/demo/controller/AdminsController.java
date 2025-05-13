package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminsController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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
        model.addAttribute("allRoles", roleService.findAll()); // Добавим роли

        return "new";
    }

    @PostMapping("/newuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public String newUser(@ModelAttribute("newUser") User user, ModelMap model) {

        Set<Role> roles = roleService.findByIds(user.getRoleIds()); // метод получения ролей по id

        if (roles == null || roles.isEmpty()) {
            model.addAttribute("errorMessage", "Роли не выбраны или не найдены.");
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

    @GetMapping("/edituser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")  //второй слой защиты
    public String editUserForm(@PathVariable("id") long id, ModelMap model) {

        User user = userService.find(id);

        if (user == null) {
            model.addAttribute("errorMessage", "Пользователь не найден");
            return "404";
        }

        model.addAttribute("updateUser", user);
        model.addAttribute("allRoles", roleService.findAll());

        return "user";
    }

    @PostMapping("/edituser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public String updateUser(@PathVariable("id") long id,
                             @ModelAttribute("updateUser") User user, ModelMap model) {

        Set<Role> roles = roleService.findByIds(user.getRoleIds());

        user.setRoles(roles);

        try {
            userService.update(id, user);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Ошибка при изменении: " + e.getMessage());
            return "404";
        }

        return "redirect:/admin";
    }

}
