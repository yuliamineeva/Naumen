package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.naumen.javakids.model.*;
import ru.naumen.javakids.services.LectureService;
import ru.naumen.javakids.services.UserLectureService;
import ru.naumen.javakids.services.UserService;

import java.security.Principal;
import java.util.*;

@Controller
public class LectureController {

    private final LectureService lectureService;
    private final UserLectureService userLectureService;
    private final UserService userService;

    @Autowired
    public LectureController(LectureService lectureService, UserLectureService userLectureService, UserService userService) {
        this.lectureService = lectureService;
        this.userLectureService = userLectureService;
        this.userService = userService;
    }

    /**
     * Страница со списком всех лекций (для админа)
     *
     * @param principal Пользователь
     * @param model     Модель для списка лекций
     * @return Список всех лекций
     */
    @GetMapping("/lectures")
    public String getAllLectures(Principal principal, Model model) {
        Set<Lecture> lectures = lectureService.getLectures();
        List<Lecture> lecturesList = new ArrayList<>(lectures);
        lecturesList.sort(Comparator.comparingLong(Lecture::getId));
        model.addAttribute("lectures", lecturesList);

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);

        return "/lecture/list";
    }

    /**
     * Страница лекции
     *
     * @param principal Пользователь
     * @param model     Модель для лекции
     * @param lectureId Id лекции
     * @return Страница лекции
     */
    @GetMapping(value = "/lecture/{id}")
    public String getLectureById(Principal principal, Model model, @PathVariable("id") Long lectureId) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);

        Optional<Lecture> lectureOp = lectureService.getLectureById(lectureId);
        if (lectureOp.isPresent()) {
            Lecture lecture = lectureOp.get();
            model.addAttribute("lecture", lecture);
            Optional<UserLecture> userLectureOp =
                    userLectureService.getUserLectureById(
                            new UserLecture.Id(userActive.getId(), lecture.getId()));
            if (userLectureOp.isPresent()) {
                UserLecture userLecture = userLectureOp.get();
                userLecture.setStatus(userLectureService.getCorrectStatus(userLecture));
                userLectureService.updateUserLecture(userLecture, userLecture.getId());
                userLectureService.saveUserLecture(userLecture);
                model.addAttribute("userlecture", userLecture);
            }
        } else {
            return "/error/page";
        }
        return "/lecture/detail";
    }

    /**
     * Обновление статуса лекции на FINISHED
     *
     * @param lectureId Id лекции
     * @param principal Пользователь
     * @return Переход на страницу лекций пользователя
     */
    @PostMapping("/lecture/{id}")
    public String finishStatusLecture(@PathVariable("id") Long lectureId, Principal principal) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        Optional<UserLecture> userLectureOp =
                userLectureService.getUserLectureById(
                        new UserLecture.Id(userActive.getId(), lectureId));
        if (userLectureOp.isPresent()) {
            UserLecture userLecture = userLectureOp.get();
            userLecture.setStatus(Status.FINISHED);
            userLectureService.saveUserLecture(userLecture);
        } else {
            return "/error/page";
        }
        return "redirect:/user/lectures";
    }

    /**
     * Страница создания лекции
     *
     * @param principal Пользователь
     * @param model     Модель для лекции
     * @return Страница создания лекции
     */
    @GetMapping("/lecture/create")
    public String createLecture(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        model.addAttribute("lecture", new Lecture());
        return "/lecture/add";
    }

    /**
     * Создание лекции
     *
     * @param lecture Лекция
     * @param model   Модель для лекции
     * @return Переход на страницу с лекциями
     */
    @PostMapping("/lecture/create")
    public String createLecture(Lecture lecture, Model model) {
        lectureService.saveLecture(lecture);
        return "redirect:/lectures";
    }

    /**
     * Страница для обновления лекции
     *
     * @param principal Пользователь
     * @param model     Модель для лекции
     * @param lectureId ID лекции
     * @return Страница для обновления лекции
     */
    @GetMapping("/lecture/{id}/update")
    public String updateLecture(Principal principal, Model model, @PathVariable("id") Long lectureId) {
        Optional<Lecture> lectureOp = lectureService.getLectureById(lectureId);
        if (lectureOp.isPresent()) {
            Lecture lecture = lectureOp.get();
            model.addAttribute("lecture", lecture);
        } else {
            return "/error/page";
        }

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        return "/lecture/update";
    }

    /**
     * Обновление лекции
     *
     * @param lectureId ID лекции
     * @param lecture   Лекция
     * @return Страница обновленной лекции
     */
    @PostMapping("/lecture/{id}/update")
    public String updateLecture(@PathVariable("id") Long lectureId, Lecture lecture) {
        lectureService.updateLecture(lecture, lectureId);
        return "redirect:/lecture/" + lectureId;
    }

    /**
     * Страница для удаления лекции
     *
     * @param principal Пользователь
     * @param model     Модель для лекции
     * @param lectureId ID лекции
     * @return Страница с иноформацией об удаленной лекции
     */
    @RequestMapping(value = "/lecture/{id}/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteLecture(Principal principal, Model model, @PathVariable("id") Long lectureId) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        Optional<Lecture> lectureOp = lectureService.getLectureById(lectureId);
        if (lectureOp.isPresent()) {
            Lecture lecture = lectureOp.get();
            model.addAttribute("lecture", lecture);
            Set<UserLecture> userLectures = userLectureService.getUserLecturesByLectureId(lectureId);
            for (UserLecture userLecture : userLectures) {
                userLectureService.deleteUserLecture(userLecture.getId());
            }
            lectureService.deleteLecture(lectureId);
        } else {
            return "/error/page";
        }
        model.addAttribute("principal", userActive);
        return "/lecture/delete";
    }

    /**
     * Страница пользователей, привязанных к лекции (для админа)
     *
     * @param principal Пользователь
     * @param model     Модель для списка лекций
     * @param lectureId ID лекции
     * @return Список пользователей, привязянных к лекции
     */
    @GetMapping("/lecture/{id}/users")
    public String getUserLectures(Principal principal, Model model, @PathVariable("id") Long lectureId) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        Optional<Lecture> lectureOp = lectureService.getLectureById(lectureId);
        if (lectureOp.isPresent()) {
            Lecture lecture = lectureOp.get();
            model.addAttribute("currentlecture", lecture);
            Set<UserLecture> userLectures = userLectureService.getUserLecturesByLectureId(lectureId);
            List<UserLecture> userLecturesList = new ArrayList<>(userLectures);
            userLecturesList.sort(Comparator.comparingLong(userLecture -> userLecture.getUser().getId()));
            model.addAttribute("userlectures", userLecturesList);
        } else {
            return "/error/page";
        }
        return "/lecture/users";
    }

    /**
     * Страница всех лекций с пользователями (для админа)
     *
     * @param principal Пользователь
     * @param model     Модель для списка всех лекций по пользователям
     * @return Страница всех лекций с пользователями
     */
    @GetMapping("/lectures/users")
    public String getUserLecturesList(Principal principal, Model model) {
        Set<Lecture> lectures = lectureService.getLectures();
        Set<UserLecture> allUserLectures = new HashSet<>();
        for (Lecture lecture : lectures) {
            Set<UserLecture> userLectures = userLectureService.getUserLecturesByLectureId(lecture.getId());
            allUserLectures.addAll(userLectures);
        }
        List<UserLecture> userLecturesList = new ArrayList<>(allUserLectures);
        userLecturesList.sort(Comparator.comparingLong(userLecture -> userLecture.getLecture().getId()));
        model.addAttribute("userlectures", userLecturesList);

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        return "/lecture/users";
    }

}
