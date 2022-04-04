package ru.naumen.javakids.services;

import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.model.Status;

import java.util.List;
import java.util.Optional;

public interface LectureService {

    List<Lecture> getLectures();

    Optional<Lecture> getLectureById(Long id);

    void saveLecture(Lecture lecture);

    void updateLecture(Lecture lecture, Long id);

    Status getCorrectStatus(Lecture lecture);

}
