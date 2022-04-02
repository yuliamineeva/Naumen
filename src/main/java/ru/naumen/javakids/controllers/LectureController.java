package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.naumen.javakids.model.Lecture;
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
    public String getLectures(Principal principal, Model model) {
        List<Lecture> lectures = lectureService.getLectures();
        model.addAttribute("lectures", lectures);

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);

        return "/lecture/list";
    }

    @GetMapping("/user/lectures")
    public String getMyLectures(Principal principal, Model model) {
        List<Lecture> lectures = lectureService.getLectures();
        model.addAttribute("lectures", lectures);

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);

        return "user/lectures";
    }

    @GetMapping(value = "/lecture/{id}")
    public String getLectureById(Principal principal, Model model, @PathVariable("id") Long lectureId) {
        Optional<Lecture> lectureOp = lectureService.getLectureById(lectureId);
        if (lectureOp.isPresent()){
            Lecture lecture = lectureOp.get();
            lecture.setStatus(Status.IN_PROCESS);
            model.addAttribute("lecture", lecture);
        }

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);

        return "lecture/detail";
    }

    @GetMapping("/lecture/create")
    public String createLecture(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);

        return "/lecture/add";
    }

    @PostMapping("/lecture/create")
    public String createLecture(Lecture lecture, Model model) {
        lecture.setStatus(Status.NOT_STARTED);
        lectureService.saveLecture(lecture);
        model.addAttribute("lecture", lecture);
        return "redirect:/lectures";
    }
}
