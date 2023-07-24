package com.example.securitry6.Service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.securitry6.Model.ApplicationUser;
import com.example.securitry6.Model.LoginResponseDTO;
import com.example.securitry6.Model.Role;
import com.example.securitry6.Repository.RoleRepository;
import com.example.securitry6.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
// Sử dụng trong quản lý giao dịch
// Để đảm bảo là tất cả thành công hoặc không có tác vụ nào được thực hiện cả 
// => đảm bảo người dùng sẽ được xác thực khi các yếu tố xác thực đều đúng/ thành công
public class AuthenticationService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    /*
     + PasswordEncoder và AuthenticationManager đóng vai trò trong việc xác thực người dùng
     + PasswordEncoder - mã hoá và so sánh mật khẩu
     + AuthenticationManager - xác thực người dùng (kiểm tra thông tin nhập vào với thông tin trong database)
       -- Xác thực thành công => tạo ra một đối tượng Authentication đại diện và chứa thông tin người dùng
    */
    private TokenService tokenService;
    
    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public ApplicationUser registerUser(String username, String password){
        String encodedPass = passwordEncoder.encode(password);
        Role role = roleRepository.findByAuthority("USER").get();
        Set<Role> authorities = new HashSet<>();
        authorities.add(role);
        
        ApplicationUser user = new ApplicationUser();
        user.setUsername(username);
        user.setPassword(encodedPass);
        user.setAuthorities(authorities);
        

        return userRepository.save(user);
    }

    public ApplicationUser registerAdmin(String username, String password){
    String encodedPass = passwordEncoder.encode(password);
        Role role = roleRepository.findByAuthority("ADMIN").get();
        Set<Role> authorities = new HashSet<>();
        authorities.add(role);
        
        ApplicationUser user = new ApplicationUser();
        user.setUsername(username);
        user.setPassword(encodedPass);
        user.setAuthorities(authorities);
        

        return userRepository.save(user);
    }

    public LoginResponseDTO loginUser(String username, String password){
        try {
            // Xác thực thông tin người dùng (username và password)
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            // So khớp == true => tạo ra một mã JWT phục vụ cho việc xác thực 2 lớp 
            String token = tokenService.generateJwt(auth);
            // Trả về một thông báo Response cho việc Login hệ thống
            return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);
        } catch (AuthenticationException e) {
            return new LoginResponseDTO(null, "");
        }
    }

    
}
