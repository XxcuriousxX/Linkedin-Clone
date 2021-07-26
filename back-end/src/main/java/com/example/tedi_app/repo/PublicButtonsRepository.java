package com.example.tedi_app.repo;

import com.example.tedi_app.model.Personalinfo;
import com.example.tedi_app.model.PublicButton;
import com.example.tedi_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicButtonsRepository extends JpaRepository<PublicButton,Long>{

    Optional<PublicButton> findByUserId(Long userId);

}
