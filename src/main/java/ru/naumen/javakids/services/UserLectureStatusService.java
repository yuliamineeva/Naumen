package ru.naumen.javakids.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.model.Status;
import ru.naumen.javakids.model.User;
import ru.naumen.javakids.model.UserLecture;
import ru.naumen.javakids.repository.LectureRepo;
import ru.naumen.javakids.repository.UserLectureStatusRepo;

import java.util.*;

@Service
public class UserLectureStatusService {

    private final UserLectureStatusRepo userLectureStatusRepo;
    private final LectureRepo lectureRepo;

    @Autowired
    public UserLectureStatusService(UserLectureStatusRepo userLectureStatusRepo, LectureRepo lectureRepo) {
        this.userLectureStatusRepo = userLectureStatusRepo;
        this.lectureRepo = lectureRepo;
    }

    public Set<UserLecture> getUserLectures() {
        Set<UserLecture> result = new HashSet<>();
        userLectureStatusRepo.findAll().forEach(result::add);
        return result;
    }

    public Set<UserLecture> getUserLecturesByUserId(User user) {
        Set<UserLecture> result = new HashSet<>();
        result.addAll(userLectureStatusRepo.findByIdUserId(user.getId()));

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
                userLectureStatusRepo.save(userLecture);
            }
        }

        return result;
    }

    public Optional<UserLecture> getUserLectureById(UserLecture.Id id) {
        return userLectureStatusRepo.findById(id);
    }

    @Transactional
    public void saveUserLecture(UserLecture userLecture) {
        userLectureStatusRepo.save(userLecture);
    }

    @Transactional
    public void updateUserLecture(UserLecture userLecture, UserLecture.Id id) {
        Optional<UserLecture> userLectureOp = userLectureStatusRepo.findById(id);

        if (userLectureOp.isPresent()) {
            UserLecture userLectureEntity = userLectureOp.get();
            userLectureEntity.setStatus(userLecture.getStatus());
        }
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
