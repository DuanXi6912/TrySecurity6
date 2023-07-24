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
public class AuthenticationService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
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
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            String token = tokenService.generateJwt(auth);
            return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);
        } catch (AuthenticationException e) {
            return new LoginResponseDTO(null, "");
        }
    }

    
}
