package ru.kata.spring.boot_security.demo.controller;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping //browser address
    public String printUsers(ModelMap model) {
            model.addAttribute("getUsers", userService.findAll()); //attribute in the index.html
        return "index"; //spring will be looking for an index.html file
    }

    @RequestMapping(value="/admin", method = RequestMethod.GET)
    public ModelAndView visitAdmin() {
        ModelAndView model = new ModelAndView("admin");
        model.addObject("title", "Admministrator Control Panel");

        return model;
    }

    @GetMapping("/newuser")
    public String createUser(ModelMap model) {
        model.addAttribute("newUser", new User());
        return "/new";
    }

    @PostMapping("/newuser")
    public String newUser(@ModelAttribute("newUser") User user) {
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/deleteuser")
    public String deleteUser(ModelMap model) {
        return "/delete";
    }

    @PostMapping("/deleteuser")
    public String removeUser(@RequestParam("id") long id) {
        try {
            userService.delete(id);
        } catch (EntityNotFoundException e) {
            return "redirect:/404";
        }
        return "redirect:/";
    }

    @GetMapping("/edituser")
    public String changeUser(ModelMap model) {
        model.addAttribute("updateUser", new User());
        return "/edit";
    }

    @PostMapping("/edituser")
    public String updateUser(@RequestParam("id") long id, @ModelAttribute("updateUser") User user) {
        try {
            userService.update(id, user);
        } catch (EntityNotFoundException e) {
            return "redirect:/404";
        }
        return "redirect:/";
    }

    @GetMapping("/404")
    public String showError(ModelMap model) {
        model.addAttribute("errorMessage", "Пользователь с таким id не найден");
        return "/404";
    }

}
