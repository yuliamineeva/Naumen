package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.naumen.javakids.services.UserService;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

/*    @GetMapping("/login")
    public String login(Principal principal, Map<String, Object> model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        if (userActive == null) {
            model.put("username", "имя пользователя");
        } else {
            model.put("username", userActive.getUsername());
        }
        return "/user/login";
    }*/

    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }
}
