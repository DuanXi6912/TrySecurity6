package com.example.securitry6.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.securitry6.Model.ApplicationUser;
import java.util.Optional;

public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {
    Optional<ApplicationUser> findByUsername(String username);
}
