package com.example.tedi_app.service;

import com.example.tedi_app.exception.UserNotFoundException;
import com.example.tedi_app.model.user;
import com.example.tedi_app.repo.UserRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.ListableBeanFactoryExtensionsKt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepo user_repo;

    @Autowired
    public UserService(UserRepo user_repo) {
        this.user_repo = user_repo;
    }

    public user addUser(user usr){
        return user_repo.save(usr);
    }

    public List<user> findAllUsers() {
        return user_repo.findAll();
    }

    public user updateUser(user u) {
        return user_repo.save(u);
    }

    public user findUserById(Long id) {
        return user_repo.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
    }
    public void deleteUser(Long id){
        user_repo.deleteUserById(id);
    }

}
