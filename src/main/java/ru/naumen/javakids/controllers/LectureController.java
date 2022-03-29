package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.model.User;
import ru.naumen.javakids.services.AddLectureService;
import ru.naumen.javakids.services.LectureService;
import ru.naumen.javakids.services.UserService;

import java.security.Principal;
import java.util.List;
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
        return "lectures";
    }

    @GetMapping("/createLecturesAndView/{topic}")
    public String createLecturesAndView(@PathVariable String topic)
    {
        lectureServices.stream()
                .filter(lectureService -> lectureService instanceof AddLectureService)
                .findFirst()
                .get().saveLecture(topic,topic);
        return "lectures";
    }
}
