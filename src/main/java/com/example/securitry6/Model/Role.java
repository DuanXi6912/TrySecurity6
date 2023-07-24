package com.example.securitry6.Model;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
// GrantedAuthority (interface) đại diện cho quyền được cấp cho người dùng trong hệ thống
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleID;
    @Column(name = "role_name")
    private String authority;

    public Role(){
        super();
    }

    public Role(String authority) {
            super();
            this.authority = authority;
    }

    public Role(int roleID, String authority) {
            super();
            this.roleID = roleID;
            this.authority = authority;
        }
    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
    
    public int getRoleID() {
        return roleID;
    }
    
    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

}
