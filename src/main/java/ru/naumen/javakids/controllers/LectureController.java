package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.naumen.javakids.model.*;
import ru.naumen.javakids.services.LectureService;
import ru.naumen.javakids.services.UserLectureStatusService;
import ru.naumen.javakids.services.UserService;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Controller
public class LectureController {

    private final LectureService lectureService;
    private final UserLectureStatusService userLectureStatusService;
    private final UserService userService;

    @Autowired
    public LectureController(LectureService lectureService, UserLectureStatusService userLectureStatusService, UserService userService) {
        this.lectureService = lectureService;
        this.userLectureStatusService = userLectureStatusService;
        this.userService = userService;
    }

    @GetMapping("/lectures")
    public String getAllLectures(Principal principal, Model model) {
        Set<Lecture> lectures = lectureService.getLectures();
        model.addAttribute("lectures", lectures);

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);

        return "/lecture/list";
    }

    @GetMapping(value = "/lecture/{id}")
    public String getLectureById(Principal principal, Model model, @PathVariable("id") Long lectureId) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);

        Optional<Lecture> lectureOp = lectureService.getLectureById(lectureId);
        if (lectureOp.isPresent()) {
            Lecture lecture = lectureOp.get();
            model.addAttribute("lecture", lecture);
            Optional<UserLecture> userLectureOp =
                    userLectureStatusService.getUserLectureById(
                            new UserLecture.Id(userActive.getId(),lecture.getId()));
            if(userLectureOp.isPresent()){
                UserLecture userLecture = userLectureOp.get();
                userLecture.setStatus(userLectureStatusService.getCorrectStatus(userLecture));
                userLectureStatusService.updateUserLecture(userLecture, userLecture.getId());
                userLectureStatusService.saveUserLecture(userLecture);
                model.addAttribute("userlecture", userLecture);
            }

        } else {
            return "/error/page";
        }

        return "lecture/detail";
    }

    @PostMapping("/lecture/{id}")
    public String finishStatusLecture(@PathVariable("id") Long lectureId, Principal principal) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        Optional<UserLecture> userLectureOp =
                userLectureStatusService.getUserLectureById(
                        new UserLecture.Id(userActive.getId(),lectureId));
        if (userLectureOp.isPresent()) {
            UserLecture userLecture = userLectureOp.get();
            userLecture.setStatus(Status.FINISHED);
            userLectureStatusService.saveUserLecture(userLecture);
        } else {
            return "/error/page";
        }
        return "redirect:/user/lectures";
    }

    @GetMapping("/lecture/create")
    public String createLecture(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);

        return "/lecture/add";
    }

    @PostMapping("/lecture/create")
    public String createLecture(Lecture lecture, Model model) {
        lectureService.saveLecture(lecture);
        model.addAttribute("lecture", lecture);
        return "redirect:/lectures";
    }

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
        if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);
        return "/lecture/update";
    }

    @PostMapping("/lecture/{id}/update")
    public String updateLecture(@PathVariable("id") Long lectureId, Lecture lecture) {
        lectureService.updateLecture(lecture, lectureId);

        return "redirect:/lecture/"+lectureId;
    }
}
