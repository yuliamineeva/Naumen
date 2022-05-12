package ru.naumen.javakids.repository;

import org.springframework.data.repository.CrudRepository;
import ru.naumen.javakids.model.User;

public interface UserRepo extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
