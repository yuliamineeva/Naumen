package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.services.LectureService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class LectureController {

    private LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value =
            "/lectures")
    @ResponseBody
    public List<Lecture> getMagicians() {
        return lectureService.getLectures();
    }

    @GetMapping("/lecturesView")
    public ModelAndView getlecturesView(){
        List<Lecture> lectures = lectureService.getLectures();
//        List<Lecture> lectures = lectureService.stream()
//                .map(lectureService -> lectureService.getLectures())
//                .flatMap(List::stream)
//                .collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView("lectures");
        modelAndView.addObject("lectures", lectures);
        return modelAndView;
    }
}
