package com.saham.hr_system.modules.auth.service;

import jakarta.mail.MessagingException;

public interface ResetPasswordEmailSender {
    /**
     * Send a password reset email to the specified recipient with the provided reset token.
     * @param recipientEmail The email address of the recipient.
     * @param resetToken The token to be included in the password reset email.
     */
    void sendEmail(String recipientEmail, String resetToken) throws MessagingException;
}
