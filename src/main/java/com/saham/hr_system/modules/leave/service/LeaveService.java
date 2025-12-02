package com.saham.hr_system.modules.leave.service;

import com.saham.hr_system.modules.leave.dto.LeaveDetailsDto;
import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.modules.leave.dto.LeaveRequestResponse;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * LeaveService interface defines all the functionalities related to leave management in the HR system.
 */
public interface LeaveService {

    /**
     * Request leave for an employee.
     * @param leaveRequestDto
     */
    void requestLeave(String email, LeaveRequestDto leaveRequestDto, MultipartFile file) throws IOException, MessagingException;

    /**
     * Get all leave requests made by an employee.
     * @param email
     * @return list of leave requests.
     */
    List<LeaveRequestResponse> getAllLeaveRequests(String email);


    /**
     * This function is responsible for fetching all the requests made by subordinates of a manager.
     * @param email
     * @return
     */
    List<LeaveRequestResponse> getAllSubordinatesRequests(String email);

    /**
     * This function returns all leave requests approved by managers and pending HR approval.
     * @return list of leave requests.
     */
    List<LeaveRequestResponse> getAllLeaveRequestsForHR();

    /**
     * Approve a leave request by its ID.
     * @param leaveRequestId
     * @param approvedBy
     */
    void approveSubordinateLeaveRequest(String approvedBy,Long leaveRequestId);

    /**
     * Reject a subordinate's leave request by its ID.
     * @param leaveRequestId
     */
    void rejectSubordinateLeaveRequest(String rejectedBy, Long leaveRequestId);

    /**
     * Final approval of leave request by HR.
     * @param leaveRequestId
     */
    void approveLeaveRequest(Long leaveRequestId);

    /**
     * This function handles leave request final rejection by HR.
     * @param leaveRequestId
     */
    void rejectLeaveRequest(Long leaveRequestId);

    /**
     * This function allows an employee to cancel a leave request by its ID.
     * @param refNumber
     */
    void cancelRequest(String refNumber);

    /**
     * This function allows an employee to cancel a leave by its ID.
     * @param refNumber
     */
    void cancelLeave(String refNumber);

    /**
     * Get all the leaves of an employee
     * @param email
     * @return
     */
    List<LeaveDetailsDto> getAllMyLeaves(String email);
}
