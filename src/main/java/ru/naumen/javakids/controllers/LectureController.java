package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.services.AddLectureService;
import ru.naumen.javakids.services.LectureService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class LectureController {

    private List<LectureService> lectureServices;

    @Autowired
    public LectureController(List<LectureService> lectureServices) {
        this.lectureServices = lectureServices;
    }

    @GetMapping("/lectures")
    public ModelAndView getLectures(){
        List<Lecture> lectures = lectureServices.stream()
                .map(lectureService -> lectureService.getLectures())
                .flatMap(List::stream)
                .collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView("lectures");
        modelAndView.addObject("lectures", lectures);
        return modelAndView;
    }

    @GetMapping("/createLecturesAndView/{topic}")
    public ModelAndView createLecturesAndView(@PathVariable String topic)
    {
        lectureServices.stream()
                .filter(lectureService -> lectureService instanceof AddLectureService)
                .findFirst()
                .get().saveLecture(topic,topic);
        return getLectures();
    }
}
