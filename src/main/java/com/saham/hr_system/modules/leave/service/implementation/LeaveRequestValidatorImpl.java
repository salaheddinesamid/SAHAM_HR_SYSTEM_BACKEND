package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.modules.leave.service.LeaveRequestValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Component
public class LeaveRequestValidatorImpl implements LeaveRequestValidator {
    @Override
    public void validate(LeaveRequestDto leaveRequestDto, MultipartFile file) {
        if (leaveRequestDto == null) throw new IllegalArgumentException("dto cannot be null");
        if (leaveRequestDto.getStartDate() == null || leaveRequestDto.getEndDate() == null)
            throw new IllegalArgumentException("startDate and endDate are required");
        if (leaveRequestDto.getEndDate().isBefore(leaveRequestDto.getStartDate()))
            throw new IllegalArgumentException("endDate must not be before startDate");
        if (leaveRequestDto.getStartDate().isBefore(LocalDate.now().minusYears(1)))
            throw new IllegalArgumentException("startDate too old");
        if(leaveRequestDto.getType().equals("EXCEPTIONAL") && leaveRequestDto.getTypeDetails().equals("SICKNESS") && file == null)
            throw new IllegalArgumentException("Medical Certificate cannot be null");
    }
}
