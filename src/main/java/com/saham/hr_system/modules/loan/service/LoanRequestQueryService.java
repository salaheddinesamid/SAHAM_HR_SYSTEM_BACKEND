package com.saham.hr_system.modules.loan.service;

import com.saham.hr_system.modules.loan.dto.LoanRequestResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * This interface provides methods for database queries.
 */
public interface LoanRequestQueryService {
    /**
     * This method returns all employee loan requests.
     * @return list of loan request details
     */
    Page<LoanRequestResponseDto> getAllEmployeeRequests(String email, int page, int size);

    /**
     *
     * @return
     */
    Page<LoanRequestResponseDto> getAllRequests(int page , int size);
}
