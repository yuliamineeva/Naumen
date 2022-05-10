package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.naumen.javakids.model.*;
import ru.naumen.javakids.services.UserLectureService;
import ru.naumen.javakids.services.UserService;

import java.security.Principal;
import java.util.*;

@Controller
public class UserController {

    private final UserService userService;
    private final UserLectureService userLectureService;

    @Autowired
    public UserController(UserService userService, UserLectureService userLectureService) {
        this.userService = userService;
        this.userLectureService = userLectureService;
    }

    /**
     * Главная страница
     * @param principal Пользователь
     * @param model Информация по пользователю для отображения
     * @return Главная страница
     */
    @GetMapping("/")
    public String getMainPage(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        if (userActive != null) {
            // Для отображения имени пользователя
            model.addAttribute("principal", userActive);

            // Для отображения меню для администратора
            if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);
        }
        return "index";
    }

    @GetMapping("/403")
    public String accessDenied(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        if (userActive != null) {
            model.addAttribute("principal", userActive);
            model.addAttribute("msg", " У вас нет прав администратора!");
        }
        return "/error/403";
    }

    /**
     * Страница обновления пользователя
     * @param principal Пользователь
     * @param model Информация по пользователю для отображения
     * @return URL user/update
     */
    @GetMapping("/user/update")
    public String updateUser(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);

        return "user/update";
    }

    /**
     * Обновление польователя
     * @param user Пользователь
     * @param id ID пользователя
     * @return Страница с обновленным пользователем
     */
    @PostMapping("/user/{id}")
    public String updateUser(@PathVariable Long id, User user) {
        userService.updateUser(id, user);

        return "redirect:/user/" + id;
    }

    /**
     * Страница с информацией по пользователю
     * @param id ID пользователя
     * @param model Модель пользователя
     * @return Страница с информацией по пользователю
     */
    @GetMapping("/user/{id}")
    public String getUserDetail(@PathVariable Long id, Model model) {
        User user = userService.loadUserById(id);
        if (user == null) {
            return "/error/page";
        } else {
            model.addAttribute("principal", user);
            if (user.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);
            return "/user/detail";
        }
    }

    /**
     * Выход из сессии
     * @return переход на страницу авторизации
     */
    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }

    /**
     * Возвращает всех пользователей
     * @principal Пользователь
     * @param model Модель для списка пользователей
     * @return URL user/list
     */
    @GetMapping("/users")
    public String getUsersList(Principal principal, Model model){
        List<User> users = userService.getUsersList();
        model.addAttribute("users", users);
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);

        return "/user/list";
    }

    /**
     * Список лекций пользователя
     * @param principal Пользователь
     * @param model Модель для списка лекций
     * @return URL user/lectures
     */
    @GetMapping("/user/lectures")
    public String getMyLectures(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        Set<UserLecture> myLectures = userLectureService.getUserLecturesByUserId(userActive);
        userActive.setUserLectures(myLectures);
        List<UserLecture> myLecturesList = new ArrayList<>(myLectures);
        myLecturesList.sort(Comparator.comparingLong(userLecture -> userLecture.getLecture().getId()));
        model.addAttribute("myLectures", myLecturesList);
        model.addAttribute("principal", userActive);
        if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);

        return "user/lectures";
    }

    /**
     * Список лекций по конкретному пользователю (у админа)
     * @param principal Пользователь
     * @param id ID лекции
     * @param model Модель для списка лекций
     * @return Список лекций по конкретному пользователю
     */
    @GetMapping("/user/{id}/lectures")
    public String getUserLectures(Principal principal, @PathVariable Long id, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);
        model.addAttribute("principal", userActive);

        User user = userService.loadUserById(id);
        if (user == null) {
            return "/error/page";
        } else {
            model.addAttribute("user", user);
            Set<UserLecture> userLectures = userLectureService.getUserLecturesByUserId(user);
            user.setUserLectures(userLectures);
            List<UserLecture> userLecturesList = new ArrayList<>(userLectures);
            userLecturesList.sort(Comparator.comparingLong(userLecture -> userLecture.getLecture().getId()));
            model.addAttribute("userLectures", userLecturesList);

            return "user/userlectures";
        }
    }

}
