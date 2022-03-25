package ru.naumen.javakids.services;

import org.springframework.stereotype.Service;
import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.model.lectures.Lecture1;
import ru.naumen.javakids.model.lectures.Lecture2;
import ru.naumen.javakids.model.lectures.Lecture3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReadyMadeLectureService implements LectureService{
    private List<Lecture> lectures;

    public ReadyMadeLectureService() {
        lectures = new ArrayList<>();
        lectures.add(new Lecture1());
        lectures.add(new Lecture2());
        lectures.add(new Lecture3());
    }

    @Override
    public List<Lecture> getLectures() {
        return lectures;
    }

    @Override
    public Lecture getLectureById(Long id) {
        List<Lecture> found =
                lectures.stream().filter(lecture -> lecture.getId() == id).collect(Collectors.toList());
        return found.size() == 0 ? null : found.stream().findFirst().get();
    }

    @Override
    public void saveLecture(String topic, String content) {

    }
}
