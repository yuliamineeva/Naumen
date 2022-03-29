package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.naumen.javakids.model.User;
import ru.naumen.javakids.services.UserService;

import java.security.Principal;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getMainPage(Principal principal, Map<String, Object> model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        if (userActive == null) {
            model.put("username", "имя пользователя");
        } else {
            model.put("user", userActive);
        }
        return "index";
    }

    @GetMapping("/user/{id}")
    public String getUserDetail(@PathVariable Long id, Model model) {
        User user = userService.loadUserById(id);
        if (user == null) {
            return "/error/page";
        } else {
            model.addAttribute("user", user);
            return "/user/detail";
        }
    }

    @GetMapping("/update")
    public String updateUser(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userActive);
        return "user/update";
    }

    @PatchMapping("/user/{id}")
    public String update(@PathVariable Long id, @RequestBody User user, Model model) {
        User userEntity = userService.updateUser(id, user);

        model.addAttribute("user", userEntity);

        return "/user/"+id;
    }
}
