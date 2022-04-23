package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.naumen.javakids.model.User;
import ru.naumen.javakids.services.UserService;

/**
 * @author avzhukov
 * @since 17.03.2022
 */
@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registrationUser() {
        return "/user/registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {

        try {
            userService.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException e) {
            userService.saveUser(user);
            model.addAttribute("message", "Пользователь успешно зарегистрирован!");
            return "/user/login";
        }

        model.addAttribute("message", "Пользователь уже существует!");
        return "/user/registration";
    }
}
