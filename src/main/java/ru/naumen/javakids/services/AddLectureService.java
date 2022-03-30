package ru.naumen.javakids.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.naumen.javakids.model.AddLecture;
import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.model.Status;
import ru.naumen.javakids.repository.LectureRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddLectureService implements LectureService {

    private LectureRepo lectureRepo;
    private List<Lecture> lectures;

    @Autowired
    public AddLectureService(LectureRepo lectureRepo) {
        this.lectureRepo = lectureRepo;
    }

    @Override
    public List<Lecture> getLectures() {
        List<Lecture> result = new ArrayList<>();
        lectureRepo.findAll().forEach(result::add);
        return result;
    }

    @Override
    public Lecture getLectureById(Long id) {
        return  lectureRepo.findById(id).get();
    }

    @Override
    public void saveLecture(String topic, String content) {
        AddLecture lecture = new AddLecture(topic, content);
        lectureRepo.save(lecture);
    }

    @Override
    public void updateStatusLecture(Long id, Status status) {
        AddLecture updLecture = lectureRepo.findById(id).get();
        updLecture.setStatus(status);
        lectureRepo.save(updLecture);
    }
}
