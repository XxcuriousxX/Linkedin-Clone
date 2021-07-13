package com.example.tedi_app;


import com.example.tedi_app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.tedi_app.model.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/user")
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<user>> getAllUsers () {
        List<user> all_users = userService.findAllUsers();
        return new ResponseEntity<>(all_users, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<user> getUserById (@PathVariable("id") Long id) {
        user usr = userService.findUserById(id);
        return new ResponseEntity<>(usr, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity  addUser(@RequestBody user usr) { user newusr =userService.addUser(usr);
        return new ResponseEntity<>(usr, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<user> updateEmployee(@RequestBody user usr) {
        user updateEmployee = userService.updateUser(usr);
        return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
