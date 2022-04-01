package ru.naumen.javakids.repository;

import org.springframework.data.repository.CrudRepository;
import ru.naumen.javakids.model.Lecture;

public interface LectureRepo extends CrudRepository<Lecture, Long> {
}
