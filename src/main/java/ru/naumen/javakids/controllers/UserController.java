package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.naumen.javakids.model.*;
import ru.naumen.javakids.services.LectureService;
import ru.naumen.javakids.services.UserExcelExporter;
import ru.naumen.javakids.services.UserLectureStatusService;
import ru.naumen.javakids.services.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UserController {

    private final UserService userService;
    private final LectureService lectureService;
    private final UserLectureStatusService userLectureStatusService;

    @Autowired
    public UserController(UserService userService, LectureService lectureService,
                          UserLectureStatusService userLectureStatusService) {
        this.userService = userService;
        this.lectureService = lectureService;
        this.userLectureStatusService = userLectureStatusService;
    }

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
    public String accesssDenied(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        if (userActive != null) {
            model.addAttribute("principal", userActive);
            model.addAttribute("msg", " У вас нет прав администратора!");
        }
        return "/error/403";
    }

    @GetMapping("/user/update")
    public String updateUser(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);

        return "user/update";
    }

    @PostMapping("/user/{id}")
    public String updateUser(@PathVariable Long id, User user) {
        userService.updateUser(id, user);

        return "redirect:/user/"+id;
    }

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

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String getUsersList(Principal principal, Model model){
        List<User> users = userService.getUsersList();
        model.addAttribute("users", users);
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);

        return "/user/list";
    }

    @GetMapping("/user/lectures")
    public String getMyLectures(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        Set<UserLecture> myLectures = userLectureStatusService.getUserLecturesByUserId(userActive);
        userActive.setUserLectures(myLectures);
        userService.saveUser(userActive);
        List<UserLecture> myLecturesList = new ArrayList<>(myLectures);
        myLecturesList.sort(Comparator.comparingLong(userLecture -> userLecture.getLecture().getId()));
        model.addAttribute("myLectures", myLecturesList);
        model.addAttribute("principal", userActive);
        if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);

        return "user/lectures";
    }

    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<User> users = userService.getUsersList();

        UserExcelExporter excelExporter = new UserExcelExporter(users);

        excelExporter.export(response);
    }
}
