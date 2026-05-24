package ru.otus.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.db.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
