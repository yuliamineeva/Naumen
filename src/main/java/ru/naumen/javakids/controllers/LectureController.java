package ru.naumen.javakids.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class LectureController {
    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        model.put("username", "пользователь");
        return "greeting";
    }
}
