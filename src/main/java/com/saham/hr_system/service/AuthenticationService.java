package com.saham.hr_system.service;

import com.saham.hr_system.dto.LoginRequestDto;
import com.saham.hr_system.dto.LoginResponseDto;

public interface AuthenticationService {

    /** This method is responsible for user authentication
     * @param requestDto
     * @return authentication token as well as Microsoft Graph details
     */
    LoginResponseDto authenticate(LoginRequestDto requestDto);
}
