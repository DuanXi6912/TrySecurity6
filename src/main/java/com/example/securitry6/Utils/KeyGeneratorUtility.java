package com.example.securitry6.Utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeyGeneratorUtility {
    // Tạo ra một cặp khoá RSA với độ dài là 2048 bit và trả về
    // Độ dài 2048 bit được coi là an toàn theo tiêu chuẩn FIPS 186-4 (độ dài ít nhất)
    public static KeyPair generateRSAKey(){
        KeyPair keyPair;
        try {
            // Try - catch: trong trường hợp hệ thống của bạn không hỗ trợ thuật toán RSA => ném ra lỗi
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException();
        }
        return keyPair;
    }
}
