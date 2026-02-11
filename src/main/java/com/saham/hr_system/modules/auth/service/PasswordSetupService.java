package com.saham.hr_system.modules.auth.service;

public interface PasswordSetupService {
    /**
     * Initiate the password setup process for a user by sending a password setup email.
     * @param email : The email of the user to set up the password for.
     * @return teh token to be used for password setup, which can be included in the email sent to the user.
     */
    String initiatePasswordSetup(String email);
    /**
     * Complete the password setup process by setting the new password for the user.
     * @param email : The email of the user to set up the password for.
     * @param newPassword : The new password to be set for the user.
     */
    void setupPassword(String email, String token, String newPassword);
}
