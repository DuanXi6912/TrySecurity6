package com.example.securitry6.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.securitry6.Model.ApplicationUser;
import com.example.securitry6.Repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
    // UserDetailsService - cung cấp một phương thức để lấy một UserDetails từ username
    // Spring Security sẽ sử dụng UserDetails này để cho việc xác thực
    @Autowired
    private UserRepository userRepository;

    public List<ApplicationUser> getUsers(){
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Load User By Username: " + username);
        return userRepository
                            .findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("user is not valid"));
    }
}
