package com.saham.hr_system.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class AuthenticationExceptionController {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity
                .status(401)
                .body(
                        Map.of("message", "Invalid email or password")
                );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {

        return ResponseEntity
                .status(404)
                .body(
                        Map.of("message", String.format("User with email %s not found", ex.getMessage()))
                );
    }

    @ExceptionHandler(PasswordResetTokenExpiredException.class)
    public ResponseEntity<Object> handleExpiredPasswordResetToken(PasswordResetTokenExpiredException ex) {
        return ResponseEntity
                .status(400)
                .body(
                        Map.of("message", "The password reset token has expired. Please request a new one.")
                );
    }

    @ExceptionHandler(OldPasswordMatchesNewException.class)
    public ResponseEntity<Object> handleOldPasswordMatchesNew(OldPasswordMatchesNewException ex) {
        return ResponseEntity
                .status(400)
                .body(
                        Map.of("message", "The new password cannot be the same as the old password.")
                );
    }
}
