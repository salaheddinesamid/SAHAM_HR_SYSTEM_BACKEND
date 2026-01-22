package com.saham.hr_system.modules.payroll.service;

public interface PayrollTokenService {
    boolean verifyToken(String token);
    String generateToken(String matriculation, int month, int year);
}
