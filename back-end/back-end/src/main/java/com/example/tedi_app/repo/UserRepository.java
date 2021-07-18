package com.example.tedi_app.repo;

import com.example.tedi_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository <User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
