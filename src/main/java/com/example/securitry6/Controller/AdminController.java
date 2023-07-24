package com.example.securitry6.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.securitry6.Model.ApplicationUser;
import com.example.securitry6.Service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/Hello")
    public String hello(){
        return "Hello Admin";
    }

    @GetMapping("/profiles")
    public List<ApplicationUser> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/profile")
    public UserDetails getUserByName(@RequestParam String username){
        return userService.loadUserByUsername(username);
    }

    @GetMapping("/myprofile")
    public UserDetails getYourProfile(){
        // SecurityContextHolder là nơi chứa thông tin người dùng (authenticated) hiện tại - tức chứa 1 
        String yourUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.loadUserByUsername(yourUsername);
    }

}
