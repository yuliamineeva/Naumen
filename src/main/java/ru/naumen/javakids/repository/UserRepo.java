package ru.naumen.javakids.repository;

import org.springframework.data.repository.CrudRepository;
import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.model.User;

import java.util.List;
import java.util.Optional;

/**
 * @author avzhukov
 * @since 17.03.2022
 */
public interface UserRepo extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
