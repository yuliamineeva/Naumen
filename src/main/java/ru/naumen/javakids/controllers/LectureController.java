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
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class LectureController {

    private List<LectureService> lectureServices;
    private final UserService userService;

    @Autowired
    public LectureController(List<LectureService> lectureServices, UserService userService) {
        this.lectureServices = lectureServices;
        this.userService = userService;
    }

    @GetMapping("/lectures")
    public String getLectures(Principal principal, Model model){
        List<Lecture> lectures = lectureServices.stream()
                .map(lectureService -> lectureService.getLectures())
                .flatMap(List::stream)
                .collect(Collectors.toList());
        model.addAttribute("lectures", lectures);

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userActive);
        return "lecturesList";
    }

    @GetMapping("/mylectures")
    public String getMyLectures(Principal principal, Model model){
        List<Lecture> lectures = lectureServices.stream()
                .map(lectureService -> lectureService.getLectures())
                .flatMap(List::stream)
                .collect(Collectors.toList());
        model.addAttribute("lectures", lectures);
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userActive);
        return "user/myLecturesList";
    }

    @GetMapping(value = "/lecture/{id}")
    public String getLectureById(Principal principal, Model model, @PathVariable("id") Long lectureId){
        Lecture lecture = lectureServices.stream()
                .map(lectureService -> lectureService.getLectureById(lectureId))
                .filter(Objects::nonNull)
                .findFirst().orElseGet(null);
        lecture.setStatus(Status.IN_PROCESS);
        model.addAttribute("lecture", lecture);
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userActive);
        return "lectures/lecture";
    }

    @PutMapping("/lecture/{id}")
    public String updateStatusLecture(@PathVariable("id") Long lectureId, @RequestBody Lecture lecture,
                                      Model model) {
        lecture.setStatus(Status.IN_PROCESS);
        lectureServices.stream()
                .filter(lectureService -> lectureService instanceof AddLectureService)
                .findFirst()
                .get().updateStatusLecture(lectureId,Status.IN_PROCESS);
        model.addAttribute("lecture", lecture);
        return "lecture/"+lectureId;
    }

    @GetMapping("/createLecturesAndView/{topic}")
    public String createLecturesAndView(@PathVariable String topic, @RequestBody Lecture lecture)
    {
        lectureServices.stream()
                .filter(lectureService -> lectureService instanceof AddLectureService)
                .findFirst()
                .get().saveLecture(topic,topic);
        return "lecturesList";
    }
}
