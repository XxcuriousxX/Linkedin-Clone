package com.example.tedi_app.service;

import com.example.tedi_app.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class JobPostService {

    private final UserRepository userRepository;

}
