package com.example.securitry6.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.example.securitry6.Utils.RSAKeyProPerties;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableMethodSecurity 
// bảo mật phương thức trong Controller, xác thực và uỷ quyền sử dụng phương thức dựa trên vai trò người dùng
// PreAuthorize
@EnableWebSecurity   
// bảo mật web, xác thực và uỷ quyền các yêu cầu HTTP dựa trên vai trò người dùng
// SecurityFilterChain
public class SecurityConfiguration {
    
    private final RSAKeyProPerties keys;

    public SecurityConfiguration(RSAKeyProPerties keys){
        this.keys = keys;
    }

    // Kiểu Encode (mã hoá) cho mật khẩu lưu vào database
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // Xác thực người dùng và tạo ra các đối tượng authentication
    public AuthenticationManager authManager(UserDetailsService detailsService){
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(detailsService);
        daoProvider.setPasswordEncoder(getPasswordEncoder());
        return new ProviderManager(daoProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            auth -> {
            auth.requestMatchers(HttpMethod.GET, "/Home/**").permitAll(); // Cho phép tất cả truy cập vào đường dẫn này
            auth.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
            }
        );
        
        http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> 
        {   
            auth.requestMatchers("/user/**").hasAnyRole("ADMIN","USER");
            auth.requestMatchers("/admin/**").hasRole("ADMIN"); 
            // Ở đây thì Spring Security có thể tự chuyển thành ROLE_ADMIN 
            // Khi bạn sử dụng phương thức hasRole() trong SecurityFilterChain, bạn chỉ cần cung cấp tên của vai trò. 
            // Spring Security sẽ tự động chuyển đổi tên của vai trò thành một đối tượng GrantedAuthority (nên không cần ROLE_)
            auth.anyRequest().authenticated();
            // Mọi yêu cầu đều phải đến từ người dùng đã được xác thực trước đó
        })
        .httpBasic(Customizer.withDefaults());
        

        http.oauth2ResourceServer(
        oauth2 -> oauth2.jwt(
        jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
            )
        );
        
        http.sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }


    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName("roles");
        converter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtconverter = new JwtAuthenticationConverter();
        jwtconverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtconverter;
    }
}
