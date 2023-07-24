package com.example.securitry6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securitry6.Model.ApplicationUser;
import com.example.securitry6.Model.LoginResponseDTO;
import com.example.securitry6.Model.RegistrationDTO;
import com.example.securitry6.Service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @PostMapping("/registerUser")
    // PreAuthorize("hasRole('ROLE_ADMIN')"") Có thể sử dụng để chỉ định phân quyền truy cập với ROLE cho phép thực hiện phương thức
    public ApplicationUser registerUser(@RequestBody RegistrationDTO newUser){
        return authenticationService.registerUser(newUser.getUsername(), newUser.getPassword());
    }

    @PostMapping("/registerAdmin")
    public ApplicationUser registerAdmin(@RequestBody RegistrationDTO newAdmin){
            return authenticationService.registerAdmin(newAdmin.getUsername(), newAdmin.getPassword());
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody RegistrationDTO user){
        return authenticationService.loginUser(user.getUsername(), user.getPassword());
    }
}
