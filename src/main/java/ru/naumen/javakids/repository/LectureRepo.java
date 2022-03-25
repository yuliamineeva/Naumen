package ru.naumen.javakids.repository;

import org.springframework.data.repository.CrudRepository;
import ru.naumen.javakids.model.AddLecture;

public interface LectureRepo extends CrudRepository<AddLecture, Long> {
}
