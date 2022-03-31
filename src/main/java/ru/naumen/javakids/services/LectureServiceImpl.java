package ru.naumen.javakids.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.model.Status;
import ru.naumen.javakids.repository.LectureRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LectureServiceImpl implements LectureService {

    private LectureRepo lectureRepo;
    private List<Lecture> lectures;

    @Autowired
    public LectureServiceImpl(LectureRepo lectureRepo) {
        this.lectureRepo = lectureRepo;
    }

    @Override
    public List<Lecture> getLectures() {
        List<Lecture> result = new ArrayList<>();
        lectureRepo.findAll().forEach(result::add);
        return result;
    }

    @Override
    public Optional<Lecture> getLectureById(Long id) {
        return lectureRepo.findById(id);
    }

    @Transactional
    @Override
    public void saveLecture(Lecture lecture) {
        lectureRepo.save(lecture);
    }

    @Transactional
    @Override
    public void updateLecture(Lecture lecture, Long id) {
        Optional<Lecture> lectureOp = lectureRepo.findById(id);

        if (lectureOp.isPresent()) {
            Lecture lectureEntity = lectureOp.get();
            lectureEntity.setTopic(lecture.getTopic());
            lectureEntity.setContent(lecture.getContent());
        }
    }

    @Override
    public void updateStatusLecture(Long id, Status status) {
        Optional<Lecture> lectureOp = lectureRepo.findById(id);
        
        if (lectureOp.isPresent()) {
            Lecture updLecture = lectureOp.get();
            updLecture.setStatus(status);
            lectureRepo.save(updLecture);
        }
    }

}