package ru.naumen.javakids.repository;

import org.springframework.data.repository.CrudRepository;
import ru.naumen.javakids.model.UserLecture;

import java.util.Set;

public interface UserLectureStatusRepo extends CrudRepository<UserLecture, UserLecture.Id> {
    public Set<UserLecture> findByIdUserId(Long userId);
}
