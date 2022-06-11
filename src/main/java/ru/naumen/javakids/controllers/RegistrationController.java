package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.naumen.javakids.model.User;
import ru.naumen.javakids.services.UserService;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    /**
     * Страница регистрации
     * @return Страница регистрации
     */
    @GetMapping("/registration")
    public String registrationUser() {
        return "registration.html";
    }

    /**
     * Регистрация нового пользователя
     * @param user Пользователь
     * @param model Модель пользователя
     * @return Переход на страницу входа или вовзрат на страницу регистрации в случае ошибки
     */
    @PostMapping("/registration")
    public String addUser(User user, Model model) {

        try {
            if (user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getEmail().isEmpty()) {
                model.addAttribute("message", "Необходимо заполнить все поля!");
                return "registration.html";
            } else {
                userService.saveUser(user);
                model.addAttribute("message", "Пользователь успешно зарегистрирован!");
                return "login.html";
            }
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("message", "Пользователь с таким логином или e-mail уже существует!");
            return "registration.html";
        }
    }
}
