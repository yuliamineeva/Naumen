package ru.naumen.javakids.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.naumen.javakids.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
