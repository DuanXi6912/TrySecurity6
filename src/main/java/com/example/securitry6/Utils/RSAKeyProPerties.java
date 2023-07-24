package com.example.securitry6.Utils;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.stereotype.Component;

@Component
public class RSAKeyProPerties {
    /*
    Trong hệ thống mã hóa RSA, cần có một cặp khóa: một khóa riêng tư (private key) và một khóa công khai (public key).
    Ở đó 
    + Khóa riêng tư được sử dụng để giải mã dữ liệu
    + Khóa công khai được sử dụng để mã hóa dữ liệu.
    */
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    
    public RSAKeyProPerties(){
        KeyPair pair = KeyGeneratorUtility.generateRSAKey();
        this.publicKey = (RSAPublicKey) pair.getPublic();
        this.privateKey = (RSAPrivateKey) pair.getPrivate();
    }

    public RSAPublicKey getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
    }

}
