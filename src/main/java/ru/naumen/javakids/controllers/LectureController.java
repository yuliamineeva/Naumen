package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.model.Status;
import ru.naumen.javakids.model.User;
import ru.naumen.javakids.services.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class LectureController {

    private final LectureService lectureService;
    private final UserService userService;

    @Autowired
    public LectureController(LectureService lectureServices, UserService userService) {
        this.lectureService = lectureServices;
        this.userService = userService;
    }

    @GetMapping("/lectures")
    public String getLectures(Principal principal, Model model) {
        List<Lecture> lectures = lectureService.getLectures();
        model.addAttribute("lectures", lectures);

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userActive);
        return "/lectures/lecturesList";
    }

    @GetMapping("/mylectures")
    public String getMyLectures(Principal principal, Model model) {
        List<Lecture> lectures = lectureService.getLectures();
        model.addAttribute("lectures", lectures);

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userActive);
        return "user/myLecturesList";
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
        model.addAttribute("user", userActive);
        return "lectures/lecture";
    }

/*    @PutMapping("/lecture/{id}")
    public String updateStatusLecture(@PathVariable("id") Long lectureId, @RequestBody Lecture lecture,
                                      Model model) {
        lecture.setStatus(Status.IN_PROCESS);
        lectureService.stream()
                .filter(lectureService -> lectureService instanceof LectureServiceImpl)
                .findFirst()
                .get().updateStatusLecture(lectureId, Status.IN_PROCESS);
        model.addAttribute("lecture", lecture);
        return "lecture/" + lectureId;
    }*/

/*    @GetMapping("/createLecturesAndView/{topic}")
    public String createLecturesAndView(@PathVariable String topic) {
        lectureServices.stream()
                .filter(lectureService -> lectureService instanceof AddLectureService)
                .findFirst()
                .get().saveLecture(topic, topic);
        return "redirect:/lectures";
    }*/

    @GetMapping("/createLecture")
    public String addLecture(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userActive);
        return "/lectures/addLecture";
    }

    @PostMapping("/createLecture")
    public String createLecture(@RequestBody Lecture lecture) {
        lectureService.saveLecture(lecture);
        return "redirect:/lectures";
    }
}
