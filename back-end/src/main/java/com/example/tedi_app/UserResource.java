package com.example.tedi_app;


import com.example.tedi_app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.tedi_app.model.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api")
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

    
     @GetMapping("/find/email/{email}")
     public ResponseEntity<user> getUserByEmail (@PathVariable("email") String email) {
         user usr = userService.findByEmail(email);
         if (usr == null) {
             System.out.println("GET FAILED");
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
         else {
            System.out.println("GET RCVD: " + usr.getEmail());
            return new ResponseEntity<>(usr, HttpStatus.OK);
         }
    }


    @PostMapping("/auth/signup")
    public ResponseEntity  addUser(@RequestBody user usr) { 

        user ret_usr = userService.findByEmail(usr.getEmail());
        

        if (ret_usr != null){
            System.out.println("ALready reported\n");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        // gia meta
        // final String encrypted_pass = bCryptPasswordEncoder.encode(usr.getPassword());

        // usr.setPassword(encrypted_pass);


        user newusr = userService.addUser(usr);
        System.out.println("New user email: " + newusr.getEmail());
        return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
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
