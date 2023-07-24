package com.example.securitry6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securitry6.Service.UserService;

@RestController
@RequestMapping("/user") // http://localhost:8080/user/** sẽ là các URL để truy cập tài nguyên với Role là USER 
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/Hello")
    public String hello(){
        return "Hello User";
    }

    @GetMapping("/profile")
        public UserDetails getUserByName(){
            String thisUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            return userService.loadUserByUsername(thisUsername);
    }
}
