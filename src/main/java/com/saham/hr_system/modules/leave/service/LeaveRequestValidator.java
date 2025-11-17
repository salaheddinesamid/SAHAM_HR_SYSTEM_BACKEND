package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface LeaveRequestValidator {
    /**
     * This method is responsible for validating the request dto, for each leave type and sub leave type
     * @param leaveRequestDto
     */
    void validate(LeaveRequestDto leaveRequestDto, MultipartFile file);
}
