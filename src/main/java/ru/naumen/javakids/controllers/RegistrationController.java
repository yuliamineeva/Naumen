package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.naumen.javakids.model.Role;
import ru.naumen.javakids.model.User;
import ru.naumen.javakids.repository.UserRepo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author avzhukov
 * @since 17.03.2022
 */
@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/registration")
    public String registrationUser() {
        return "/user/registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.addAttribute("message", "Пользователь уже существует!");
            return "/user/registration";
        }

        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles(roles);
        userRepo.save(user);

        model.addAttribute("message", "Пользователь успешно зарегистрирован!");
        return "/user/login";
    }
}
