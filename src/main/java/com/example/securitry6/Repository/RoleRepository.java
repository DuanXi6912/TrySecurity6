package com.example.securitry6.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.securitry6.Model.Role;
import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Integer>{
    Optional<Role> findByAuthority(String authority);
}
