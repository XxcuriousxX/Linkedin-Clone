package com.example.tedi_app.repo;
import com.example.tedi_app.model.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<user,Long> {
    void deleteUserById(Long id);
    Optional<user> findUserById(Long id);
}
