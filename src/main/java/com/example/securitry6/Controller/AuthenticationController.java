package com.example.securitry6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securitry6.Model.ApplicationUser;
import com.example.securitry6.Model.RegistrationDTO;
import com.example.securitry6.Service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @PostMapping("/registerUser")
    public ApplicationUser registerUser(@RequestBody RegistrationDTO newUser){
        return authenticationService.registerUser(newUser.getUsername(), newUser.getPassword());
    }

    @PostMapping("/registerAdmin")
    public ApplicationUser registerAdmin(@RequestBody RegistrationDTO newAdmin){
            return authenticationService.registerAdmin(newAdmin.getUsername(), newAdmin.getPassword());
        }
}
