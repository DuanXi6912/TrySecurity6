package com.example.securitry6.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securitry6.Model.ApplicationUser;
import com.example.securitry6.Repository.UserRepository;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserRepository  userRepository;
    
    @GetMapping("/Hello")
    public String hello(){
        return "Hello User";
    }

    @GetMapping("/profiles")
    public List<ApplicationUser> getUser(){
        return userRepository.findAll();
    }
}
