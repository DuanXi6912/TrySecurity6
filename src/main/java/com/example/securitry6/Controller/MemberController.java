package com.example.securitry6.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Home")
public class MemberController {
    
    @GetMapping("/hello")
    public String hello(){
        return "Hello, Login for more";
    }
    
}
