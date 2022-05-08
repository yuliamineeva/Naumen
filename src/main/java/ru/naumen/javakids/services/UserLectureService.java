package ru.naumen.javakids.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.model.Status;
import ru.naumen.javakids.model.User;
import ru.naumen.javakids.model.UserLecture;
import ru.naumen.javakids.repository.LectureRepo;
import ru.naumen.javakids.repository.UserLectureRepo;

import java.util.*;

@Service
public class UserLectureService {

    private final UserLectureRepo userLectureRepo;
    private final LectureRepo lectureRepo;

    @Autowired
    public UserLectureService(UserLectureRepo userLectureRepo, LectureRepo lectureRepo) {
        this.userLectureRepo = userLectureRepo;
        this.lectureRepo = lectureRepo;
    }

    public Set<UserLecture> getUserLectures() {
        Set<UserLecture> result = new HashSet<>();
        userLectureRepo.findAll().forEach(result::add);
        return result;
    }

    public Set<UserLecture> getUserLecturesByUserId(User user) {
        Set<UserLecture> result = new HashSet<>(userLectureRepo.findByIdUserId(user.getId()));

        Set<Lecture> lectures = new HashSet<>();
        lectureRepo.findAll().forEach(lectures::add);

        for (Lecture lecture : lectures) {
            Optional<UserLecture> userLectureOp = getUserLectureById(new UserLecture.Id(user.getId(), lecture.getId()));
            if (userLectureOp.isPresent()) {
                UserLecture userLecture = userLectureOp.get();
                userLecture.setStatus(userLecture.getStatus());
                saveUserLecture(userLecture);
            } else {
                UserLecture userLecture = new UserLecture(user, lecture, Status.NOT_STARTED);
                result.add(userLecture);
                userLectureRepo.save(userLecture);
            }
        }
        return result;
    }

    public Set<UserLecture> getUserLecturesByLectureId(Long lectureId) {
        return new HashSet<>(userLectureRepo.findByIdLectureId(lectureId));
    }

    public Optional<UserLecture> getUserLectureById(UserLecture.Id id) {
        return userLectureRepo.findById(id);
    }

    @Transactional
    public void saveUserLecture(UserLecture userLecture) {
        userLectureRepo.save(userLecture);
    }

    @Transactional
    public void updateUserLecture(UserLecture userLecture, UserLecture.Id id) {
        Optional<UserLecture> userLectureOp = userLectureRepo.findById(id);

        if (userLectureOp.isPresent()) {
            UserLecture userLectureEntity = userLectureOp.get();
            userLectureEntity.setStatus(userLecture.getStatus());
        }
    }

    @Transactional
    public void deleteUserLecture(UserLecture.Id id) {
        userLectureRepo.deleteById(id);
    }

    public Status getCorrectStatus(UserLecture userLecture) {
        Status currentStatus = userLecture.getStatus();
        if (currentStatus == null) {
            return Status.NOT_STARTED;
        } else if (currentStatus == Status.FINISHED) {
            return Status.FINISHED;
        }
        return Status.IN_PROCESS;
    }
}
