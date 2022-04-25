package ru.naumen.javakids.repository;

import org.springframework.data.repository.CrudRepository;
import ru.naumen.javakids.model.UserLecture;

import java.util.Set;

public interface UserLectureRepo extends CrudRepository<UserLecture, UserLecture.Id> {
    Set<UserLecture> findByIdUserId(Long userId);
    Set<UserLecture> findByIdLectureId(Long lectureId);
}
