package com.saham.hr_system.modules.auth.controller;

import com.saham.hr_system.modules.auth.dto.LoginRequestDto;
import com.saham.hr_system.modules.auth.service.implementation.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    @Autowired
    public AuthenticationController(AuthenticationServiceImpl authenticationServiceImpl) {
        this.authenticationServiceImpl = authenticationServiceImpl;
    }

    @PostMapping("")
    public ResponseEntity<?> authentication(@RequestBody LoginRequestDto requestDto){
        return ResponseEntity
                .status(200)
                .body(authenticationServiceImpl.authenticate(requestDto));
    }
}
