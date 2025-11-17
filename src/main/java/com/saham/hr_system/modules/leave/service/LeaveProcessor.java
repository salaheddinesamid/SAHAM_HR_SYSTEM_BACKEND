package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface LeaveProcessor {

    boolean supports(String leaveType);
    LeaveRequest process(String email, LeaveRequestDto requestDto, MultipartFile file) throws IOException;
}
