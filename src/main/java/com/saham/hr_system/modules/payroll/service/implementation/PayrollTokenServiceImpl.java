package com.saham.hr_system.modules.payroll.service.implementation;

import com.saham.hr_system.modules.payroll.service.PayrollTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import static java.time.Instant.now;

@Service
public class PayrollTokenServiceImpl implements PayrollTokenService {
    @Value("${secret.key}")
    private String KEY;
    @Override
    public boolean verifyToken(String token) {
        return false;
    }

    @Override
    public String generateToken(String matriculation, int month, int year) {
        return
                Jwts
                        .builder()
                        .setSubject("PAYROLL_DOWNLOAD")
                        .claim("matriculation", matriculation)
                        .claim("month", month)
                        .claim("year", year)
                        .signWith(SignatureAlgorithm.HS256, KEY)
                        .setIssuedAt(Date.from(now()))
                        .setExpiration(Date.from(now().plus(5, ChronoUnit.MINUTES)))
                        .compact();

    }
}
