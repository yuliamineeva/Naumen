package ru.naumen.javakids.services;

import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.model.Status;

import java.util.List;

public interface LectureService {

    List<Lecture> getLectures();

    Lecture getLectureById(Long id);

    void saveLecture(String topic, String content);

    void updateStatusLecture(Long id, Status status);
}
