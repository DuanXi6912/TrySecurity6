package com.example.securitry6.Model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "person_infor")
@Getter
@Setter
public class ApplicationUser implements UserDetails {
    // UserDetails - lớp trừu tượng đại diện cho thông tin chi tiết người dùng 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
        name = "person_infor_role",
        joinColumns = @JoinColumn(name ="id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> authorities;

    public ApplicationUser(){
        super();
        this.authorities = new HashSet<Role>();
    }

    public ApplicationUser(int id, String username, String password, Set<Role> authorities){
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public int getUserID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public void setAuthorities(Set<Role> authorities){
        this.authorities = authorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;    
    }

    @Override
    public String getPassword() {
        return this.password;    
    }

    @Override
    public String getUsername() {
        return this.username;    
    }

    // Trạng thái hết hạn của tài khoản
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Trạng thái khoá của tài khoản 
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Trạng thái hết hạn mật khẩu của tài khoản
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Trạng thái kích hoạt của tài khoản
    @Override
    public boolean isEnabled() {
        return true;
    }

}
