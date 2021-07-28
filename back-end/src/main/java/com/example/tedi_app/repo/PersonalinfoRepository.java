package com.example.tedi_app.repo;

import com.example.tedi_app.model.Personalinfo;
import com.example.tedi_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalinfoRepository extends JpaRepository<Personalinfo,Long> {

    Optional<Personalinfo> findByUserId(Long userId);

    Optional<Personalinfo> findByUsername(String username);

}
