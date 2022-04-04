package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.model.Role;
import ru.naumen.javakids.model.Status;
import ru.naumen.javakids.model.User;
import ru.naumen.javakids.services.LectureService;
import ru.naumen.javakids.services.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class LectureController {

    private final LectureService lectureService;
    private final UserService userService;

    @Autowired
    public LectureController(LectureService lectureService, UserService userService) {
        this.lectureService = lectureService;
        this.userService = userService;
    }

    @GetMapping("/lectures")
    public String getAllLectures(Principal principal, Model model) {
        List<Lecture> lectures = lectureService.getLectures();
        model.addAttribute("lectures", lectures);

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        if (userActive.getRoles().contains(Role.ADMIN)) model.addAttribute("master", Role.ADMIN);

        return "/lecture/list";
    }

    @GetMapping("/user/lectures")
    public String getMyLectures(Principal principal, Model model) {
        List<Lecture> lectures = lectureService.getLectures();
        model.addAttribute("lectures", lectures);

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        if (userActive.getRoles().contains(Role.ADMIN)) model.addAttribute("master", Role.ADMIN);

        return "user/lectures";
    }

    @GetMapping(value = "/lecture/{id}")
    public String getLectureById(Principal principal, Model model, @PathVariable("id") Long lectureId) {
        Optional<Lecture> lectureOp = lectureService.getLectureById(lectureId);
        if (lectureOp.isPresent()) {
            Lecture lecture = lectureOp.get();
            lecture.setStatus(lectureService.getCorrectStatus(lecture));
            lectureService.saveLecture(lecture);
            model.addAttribute("lecture", lecture);
        } else {
            return "/error/page";
        }

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        if (userActive.getRoles().contains(Role.ADMIN)) model.addAttribute("master", Role.ADMIN);

        return "lecture/detail";
    }


    @PostMapping("/lecture/{id}")
    public String finishStatusLecture(@PathVariable("id") Long lectureId) {
        Optional<Lecture> lectureOp = lectureService.getLectureById(lectureId);
        if (lectureOp.isPresent()) {
            Lecture lecture = lectureOp.get();
            lecture.setStatus(Status.FINISHED);
            lectureService.saveLecture(lecture);
        } else {
            return "/error/page";
        }
        return "redirect:/user/lectures";
    }

    @GetMapping("/lecture/create")
    public String createLecture(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
        if (userActive.getRoles().contains(Role.ADMIN)) model.addAttribute("master", Role.ADMIN);

        return "/lecture/add";
    }

    @PostMapping("/lecture/create")
    public String createLecture(Lecture lecture, Model model) {
        lecture.setStatus(Status.NOT_STARTED);
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
        if (userActive.getRoles().contains(Role.ADMIN)) model.addAttribute("master", Role.ADMIN);
        return "/lecture/update";
    }

    @PostMapping("/lecture/{id}/update")
    public String updateLecture(@PathVariable("id") Long lectureId, Lecture lecture) {
        lectureService.updateLecture(lecture, lectureId);

        return "redirect:/lecture/"+lectureId;
    }
}
