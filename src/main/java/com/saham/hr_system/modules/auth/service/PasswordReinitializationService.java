package com.saham.hr_system.modules.auth.service;

public interface PasswordReinitializationService {
    /**
     * Initiates the password reset process for a user by their email.
     * @param email The email of the user requesting password reset.
     * @return A message indicating the result of the initiation process.
     */
    String initiatePasswordReset(String email);

    /**
     * Resets the user's password using a provided token and new password.
     * @param token The token received by the user for password reset.
     * @param newPassword The new password to set for the user.
     */
    void resetPassword(String token, String newPassword);
}
