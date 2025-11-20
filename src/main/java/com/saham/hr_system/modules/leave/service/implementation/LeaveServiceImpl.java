package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.modules.leave.dto.LeaveRequestResponse;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;

import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.LeaveApproval;
import com.saham.hr_system.modules.leave.service.LeaveProcessor;
import com.saham.hr_system.modules.leave.service.LeaveService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestValidatorImpl leaveRequestValidator;
    private final List<LeaveProcessor> processors;
    private final List<LeaveApproval> approvals;

    @Autowired
    public LeaveServiceImpl(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository, LeaveRequestValidatorImpl leaveRequestValidator, List<LeaveProcessor> processors, List<LeaveApproval> approvals) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.leaveRequestValidator = leaveRequestValidator;
        this.processors = processors;
        this.approvals = approvals;
    }

    @Override
    @Transactional
    public void requestLeave(String email , LeaveRequestDto leaveRequestDto, MultipartFile file) throws IOException, MessagingException {
        // validate the request fields:
        leaveRequestValidator.validate(leaveRequestDto, file);
        // process the request
        LeaveProcessor processor =
                processors.stream().filter(p-> p.supports(leaveRequestDto.getType()))
                        .findFirst().orElse(null);

        assert processor != null;
        processor.process(email,leaveRequestDto, file);
    }

    @Override
    public List<LeaveRequestResponse> getAllLeaveRequests(String email) {

        // Fetch the employee from db:
        Employee employee =
                employeeRepository.findByEmail(email).orElseThrow();

        List<LeaveRequest> requests = leaveRequestRepository.findAllByEmployee(employee);

        return requests.stream().map(LeaveRequestResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestResponse> getAllSubordinatesRequests(String email) {
        // Fetch the manager:
        Employee manager = employeeRepository
                .findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        // Fetch the subordinates
        List<Employee> subordinates = employeeRepository.findAllByManagerId(manager.getId());

        // Fetch leave requests (IN PROCESS ONLY):
        List<LeaveRequest> requests = subordinates.stream()
                .flatMap(employee -> leaveRequestRepository.findAllByEmployeeAndStatusAndApprovedByManager(employee, LeaveRequestStatus.IN_PROCESS, false).stream())
                .toList();


        return requests.isEmpty() ? List.of() : requests.stream()
                .map(LeaveRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestResponse> getAllLeaveRequestsForHR() {
        // Fetch all leave requests that are pending and approved by managers:
        List<LeaveRequest> requests =
                leaveRequestRepository.findAllByStatusAndApprovedByManager(
                        LeaveRequestStatus.IN_PROCESS, true
                );

        // return a response dto:
        return
                requests.stream().map(LeaveRequestResponse::new).collect(Collectors.toList());
    }

    @Override
    public void approveSubordinateLeaveRequest(String approvedBy,Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow();
        LeaveApproval approval =
                approvals.stream().filter(p-> p.supports(leaveRequest.getTypeOfLeave().toString()))
                .findFirst().orElse(null);
        // approve:
        assert approval != null;
        approval.approveSubordinate(approvedBy,leaveRequestId);
    }

    @Override
    @Transactional
    public void approveLeaveRequest(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow();
        LeaveApproval approval =
                approvals.stream().filter(p-> p.supports(leaveRequest.getTypeOfLeave().toString()))
                        .findFirst().orElse(null);
        // approve:
        assert approval != null;
        approval.approve(leaveRequestId);

    }

    @Override
    public void rejectSubordinateLeaveRequest(String rejectedBy,Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow();
        LeaveApproval approval =
                approvals.stream().filter(p-> p.supports(leaveRequest.getTypeOfLeave().toString()))
                        .findFirst().orElse(null);
        // approve:
        assert approval != null;
        approval.rejectSubordinate(rejectedBy ,leaveRequestId);
    }

    @Override
    public void rejectLeaveRequest(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow();
        LeaveApproval approval =
                approvals.stream().filter(p-> p.supports(leaveRequest.getTypeOfLeave().toString()))
                        .findFirst().orElse(null);
        // approve:
        assert approval != null;
        approval.rejectLeave(leaveRequestId);
    }

    // Notify employee of leave request approval or rejection:
    private void notifyEmployee(String email){

    }

    // Notify the manager of subordinate's leave request or approval/rejection:
    private void notifyManager(String email){

    }
}
