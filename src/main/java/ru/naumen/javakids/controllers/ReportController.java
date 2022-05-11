package ru.naumen.javakids.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.model.User;
import ru.naumen.javakids.model.UserLecture;
import ru.naumen.javakids.services.LectureService;
import ru.naumen.javakids.services.ReportExcelExporter;
import ru.naumen.javakids.services.UserLectureService;
import ru.naumen.javakids.services.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ReportController {

    private final LectureService lectureService;
    private final UserLectureService userLectureService;
    private final UserService userService;

    @Autowired
    public ReportController(LectureService lectureService, UserLectureService userLectureService,
                            UserService userService) {
        this.lectureService = lectureService;
        this.userLectureService = userLectureService;
        this.userService = userService;
    }

    /**
     * Загрузка списка пользователей в Excel
     *
     * @param response объект http сервлета
     */
    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        configureResponse(response, "users");
        List<User> users = userService.getUsersList();
        ReportExcelExporter excelExporter = new ReportExcelExporter(users, "Users");
        excelExporter.exportToExcel(response, "users");
    }

    /**
     * Загрузка списка лекций в Excel
     *
     * @param response объект http сервлета
     */
    @GetMapping("/lectures/export/excel")
    public void exportToExcelLectures(HttpServletResponse response) throws IOException {
        configureResponse(response, "lectures");
        Set<Lecture> lectures = lectureService.getLectures();
        List<Lecture> lecturesList = new ArrayList<>(lectures);
        lecturesList.sort(Comparator.comparingLong(Lecture::getId));
        ReportExcelExporter excelExporter = new ReportExcelExporter(lecturesList, "Lectures");
        excelExporter.exportToExcel(response, "lectures");
    }

    /**
     * Загрузка списка всех лекций по всем пользователям в Excel
     *
     * @param response объект http сервлета
     */
    @GetMapping("/lectures/users/export/excel")
    public void exportToExcelUserLectures(HttpServletResponse response) throws IOException {
        configureResponse(response, "userlectures");
        Set<UserLecture> allUserLectures = userLectureService.getUserLectures();
        List<UserLecture> userLecturesList = new ArrayList<>(allUserLectures);
        userLecturesList.sort(Comparator.comparingLong(userLecture -> userLecture.getLecture().getId()));
        ReportExcelExporter excelExporter = new ReportExcelExporter(userLecturesList, "UserLectures");
        excelExporter.exportToExcel(response, "userLectures");
    }

    /**
     * Создание параметров файла для выгрузки
     *
     * @param response объект http сервлета
     * @param filename имя файла
     */
    private void configureResponse(HttpServletResponse response, String filename) {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + filename +"_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
    }
}
