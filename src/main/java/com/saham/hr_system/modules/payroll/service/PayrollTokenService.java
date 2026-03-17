package com.saham.hr_system.modules.payroll.service;
/** * Service interface for managing payroll tokens, including generation and verification.
 */
public interface PayrollTokenService {
    /**
     * Verifies the validity of a given payroll token.
     *
     * @param token the payroll token to verify
     * @return true if the token is valid, false otherwise
     */
    boolean verifyToken(String token);
    /**
     * Generates a payroll token based on the provided employee matriculation number, month, and year.
     *
     * @param matriculation the employee's matriculation number
     * @param month the month for which the token is generated
     * @param year the year for which the token is generated
     * @return a generated payroll token as a String
     */
    String generateToken(String matriculation, int month, int year);
}
