package com.example.securitry6.Service;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    @Autowired
    private JwtEncoder jwtEncoder;

    // Tạo ra một JWT dựa trên thông tin xác thực được cung cấp
    public String generateJwt(Authentication auth){
        // Lấy thời gian hiện tại
        Instant now = Instant.now();
        // Chuỗi scope được tạo từ danh sách các quyền của Authentication
        // example scope: "ROLE_ADMIN ROLE_USER"
        String scope = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        
        // Tạo ra một đối tượng claims bao gồm các thông tin cần thiết để tạo ra một JWT
        JwtClaimsSet claims = JwtClaimsSet.builder()  // Tạo ra một hộp chứa
                                                    .issuer("self")             // đặt thông tin về người phát hành là chính chúng ta - self
                                                    .issuedAt(now)             // đặt thời gian phát hành là bây giờ - now
                                                    .subject(auth.getName())  // đặt thông tin về chủ sở hữu - username
                                                    .claim("roles", scope)   // thêm thông tin về quyền "roles" với giá trị là chuỗi scope
                                                    .build();                // hoàn thiện đối tượng và đóng gói lại => thu được một mã JWT
       
        // jwtEnconder mã hoá JwtClaimsSet và trả về giá trị của token
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
