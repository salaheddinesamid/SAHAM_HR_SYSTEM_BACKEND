package com.saham.hr_system.modules.loan.service;

import com.saham.hr_system.modules.loan.dto.LoanRequestResponseDto;

import java.util.List;

/**
 * This interface provides methods for database queries.
 */
public interface LoanRequestQueryService {
    /**
     * This method returns all employee loan requests.
     * @return list of loan request details
     */
    List<LoanRequestResponseDto> getAllEmployeeRequests(String email);

    /**
     *
     * @return
     */
    List<LoanRequestResponseDto> getAllPendingRequests();
}
