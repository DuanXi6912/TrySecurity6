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
        // đối tượng DaoAuthenticationProvider là một cơ chế xác thực trong Spring Security để xác minh thông tin đăng nhập của người dùng
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(detailsService);

        // gọi phương thức PasswordEncoder để lấy một đối tượng mã hoá mật khẩu 
        // đối tượng này có thể mã hoá mật khẩu trước khi so sánh với mật khẩu đã lưu
        daoProvider.setPasswordEncoder(getPasswordEncoder());
        // Trả về một đối tượng ProvideManager để quản lý quá trình xác thực
        return new ProviderManager(daoProvider);
    }

    // Xử lý bảo mật cho một ứng dụng HTTP, 
    // cấu hình các thiết lập bảo mật cho ứng dụng
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http
        .csrf(csrf -> csrf.disable()) // tắt bảo vệ csrf - một tính năng bảo mật ngăn chặn các cuộc gọi tấn công giả mạo yêu cầu từ các trang web khác?
        // Thiết lập các quy tắc uỷ quền cho các loại yêu cầu HTTP
        // Các phương thức hasAnyRole và hasRole xác định các vai trò yêu cầu để truy cập đường dẫn cụ thể
        // anyRequest().authenticated() - đảm bảo các yêu cầu từ "người dùng đã xác thực"
        // httpBasic cấu hình xác thực HTTP Basic, một cơ chế xác thực đơn giản yêu cầu username và password
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
        
        // oauth2ResourceServer cho phép chức năng máy chủ tài nguyên OAuth2 với xác thực JWT
        // + Ở đây sử dụng một bộ chuyển đổi tuỳ chỉnh "jwtAuthenticationConverter()" để chuyển đổi thôgn báo JWT thành một đối tượng xác thực

        http.oauth2ResourceServer(
        oauth2 -> oauth2.jwt(
        jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
            )
        );
        // phương thức sessionManagement đặt chính sách tạo phiên là không có trạng thái
        // có nghĩa là máy chủ không lưu trữ bất kỳ thông tin phiên nào cho khách hàng ??
        http.sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        // phương thức build xây dựng cấu hình cuối cùng và trả về đối tượng 
        return http.build();
    }


    // Trả về một đối tượng JwtDecoder 
    // + Được tạo ra bằng cách sử dụng lớp NimbusJwtDecoder, được cấu hình với một getPublicKey
    // + Đối tượng này dùng để giải mã và xác thực chuỗi JWT
    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        // Tạo ra một đối tượng JWK (JSON Web Key) từ cặp khoá, nó chứa thông tin về publicKey và privateKey
        JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
        // Sử dụng đối tượng JWK để tạo ra một JWKSource 
        // JWKSource cung cấp thông tin về khoá để mã hoá và giải mã JWT
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
        // Trả về đối tượng NimbusJwtEncoder được sử dụng để mã hoá JWT bằng cách sử dụng khoá đã được cung cấp
    }

    // Nhiệm vụ là chuyển đổi đoạn mã JWT thành một đối tượng xác thực
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        // Đối tượng converter này sử dụng để trích xuất các quyền từ đoạn mã JWT
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        // Ta đặt tên cho các thuộc tính chứa danh sách các quyền là "roles"
        converter.setAuthoritiesClaimName("roles");
        // Đặt tiền tố "ROLE_" cho mỗi giá trị quyền
        converter.setAuthorityPrefix("ROLE_");
        // Đối tượng jwtconverter chịu trách nhiệm chuyển đổi đoạn mã JWT thành một đối tượng xác thực 
        JwtAuthenticationConverter jwtconverter = new JwtAuthenticationConverter();
        // với cấu hình từ đối tượng converter cho nó
        jwtconverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtconverter;
    }
}
