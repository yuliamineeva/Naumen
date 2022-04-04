package ru.naumen.javakids.services;

import ru.naumen.javakids.model.Lecture;

import java.util.Optional;
import java.util.Set;

public interface LectureService {

    Set<Lecture> getLectures();

    Optional<Lecture> getLectureById(Long id);

    void saveLecture(Lecture lecture);

    void updateLecture(Lecture lecture, Long id);

}
