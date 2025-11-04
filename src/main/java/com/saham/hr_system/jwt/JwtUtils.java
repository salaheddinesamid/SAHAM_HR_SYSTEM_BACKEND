package com.saham.hr_system.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtUtils {

    @Value("${secret.key}")
    private String secretKey;

    private final Long jwtExpiration = 36000000L; // 10 hours

    public String generateToken(String email, List<String> roles) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
