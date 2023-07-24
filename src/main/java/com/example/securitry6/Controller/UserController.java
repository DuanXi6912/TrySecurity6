package com.example.securitry6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securitry6.Service.UserService;

@RestController
@RequestMapping("/user") // http://localhost:8080/user/** sẽ là các URL để truy cập tài nguyên
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/Hello")
    public String hello(){
        return "Hello User";
    }

    @GetMapping("/profile")
        public UserDetails getUserByName(){
            /*
             -- Khi người dùng đăng nhập thành công: 
                + Thông tin người dùng đó được lưu trữ trong một đối tượng gọi là SecurityContext 
                + SecurityContextHolder là nơi lưu trữ SecurityContext này
             -- Phương thức getAuthentication() trả về thông tin xác thực của người dùng hiện tại
             -- Phương thức getName() cho phép ta lấy ra username của người dùng
             => ta có cách để lấy ra username của người dùng đang được xác thực trong hệ thống để thực hiện phương thức loadUserByUsername
             */
            String thisUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            return userService.loadUserByUsername(thisUsername);
    }
}
