package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "Пользователь уже существует!");
            return "registration";
        }

        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);

        user.setActive(true);
        user.setRoles(roles);
        userRepo.save(user);

        return "redirect:/login";
    }
}
