package com.fpt.edu.controller;

import com.fpt.edu.entities.User;
import com.fpt.edu.services.UserServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    private final UserServices userServices;

    public AuthenticationController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("new")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        User result = userServices.addNewUser(user);
        return ResponseEntity.ok().body(result);
    }
}
