package com.saham.hr_system.modules.auth.controller;

import com.saham.hr_system.modules.auth.dto.LoginRequestDto;
import com.saham.hr_system.modules.auth.service.implementation.AuthenticationServiceImpl;
import com.saham.hr_system.modules.auth.service.implementation.EmployeePasswordReinitialization;
import com.saham.hr_system.modules.auth.service.implementation.EmployeePasswordSetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationServiceImpl;
    private final EmployeePasswordReinitialization employeePasswordReinitialization;
    private final EmployeePasswordSetupService employeePasswordSetupService;

    @Autowired
    public AuthenticationController(AuthenticationServiceImpl authenticationServiceImpl, EmployeePasswordReinitialization employeePasswordReinitialization, EmployeePasswordSetupService employeePasswordSetupService) {
        this.authenticationServiceImpl = authenticationServiceImpl;
        this.employeePasswordReinitialization = employeePasswordReinitialization;
        this.employeePasswordSetupService = employeePasswordSetupService;
    }

    @PostMapping("")
    public ResponseEntity<?> authentication(@RequestBody LoginRequestDto requestDto){
        return ResponseEntity
                .status(200)
                .body(authenticationServiceImpl.authenticate(requestDto));
    }

    @PostMapping("forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestParam String email){
        employeePasswordReinitialization.initiatePasswordReset(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("reset-password")
    public ResponseEntity<Object> resetPassword(@RequestParam String token, @RequestParam String newPassword){
        employeePasswordReinitialization.resetPassword(token, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("setup-password")
    public ResponseEntity<Object> setupPassword(@RequestParam String token, @RequestParam String newPassword){
        employeePasswordSetupService.setupPassword(token, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("re-activate-account")
    public ResponseEntity<Object> reActivateAccount(@RequestParam String email){
        employeePasswordSetupService.initiatePasswordSetup(email);
        return ResponseEntity.ok().build();
    }
}
