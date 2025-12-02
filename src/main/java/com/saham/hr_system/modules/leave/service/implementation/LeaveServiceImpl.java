package com.saham.hr_system.modules.leave.service.implementation;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.leave.dto.LeaveDetailsDto;
import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.modules.leave.dto.LeaveRequestResponse;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;

import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
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
    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestValidatorImpl leaveRequestValidator;
    private final List<LeaveProcessor> processors;
    private final List<LeaveApproval> approvals;
    private final LeaveRequestCancelerImpl leaveRequestCanceler;
    private final LeaveCancellerImpl leaveCanceller;

    @Autowired
    public LeaveServiceImpl(LeaveRequestRepository leaveRequestRepository, LeaveRepository leaveRepository, EmployeeRepository employeeRepository, LeaveRequestValidatorImpl leaveRequestValidator, List<LeaveProcessor> processors, List<LeaveApproval> approvals, LeaveRequestCancelerImpl leaveRequestCanceler, LeaveCancellerImpl leaveCanceller) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
        this.leaveRequestValidator = leaveRequestValidator;
        this.processors = processors;
        this.approvals = approvals;
        this.leaveRequestCanceler = leaveRequestCanceler;
        this.leaveCanceller = leaveCanceller;
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

    /**
     * This method handles endpoint request to cancel a leave request.
     * @param refNumber
     */
    @Override
    public void cancelRequest(String refNumber) {
        leaveRequestCanceler.cancel(refNumber);
    }

    /**
     * This method handles endpoint request to cancel a leave.
     * @param refNumber
     */
    @Override
    public void cancelLeave(String refNumber) {
        leaveCanceller.cancel(refNumber);
    }

    @Override
    public List<LeaveDetailsDto> getAllMyLeaves(String email){
        // Fetch the employee from db:
        Employee employee =
                employeeRepository.findByEmail(email).orElseThrow();

        List<Leave> leaves = leaveRepository.findAllByEmployee(employee);

        return leaves.stream().map(LeaveDetailsDto::new).collect(Collectors.toList());
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
                .flatMap(employee -> leaveRequestRepository.findAllByEmployee(employee).stream())
                .toList();
        return requests.isEmpty() ? List.of() : requests.stream()
                .map(LeaveRequestResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestResponse> getAllLeaveRequestsForHR() {
        // Fetch all leave requests for HR:
        List<LeaveRequest> requests =
                leaveRequestRepository.findAllByApprovedByManagerOrStatusOrStatus(
                        true,
                        LeaveRequestStatus.APPROVED,
                        LeaveRequestStatus.REJECTED
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
