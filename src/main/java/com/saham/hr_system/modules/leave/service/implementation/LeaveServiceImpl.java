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
import com.saham.hr_system.modules.leave.service.LeaveCanceller;
import com.saham.hr_system.modules.leave.service.LeaveProcessor;
import com.saham.hr_system.modules.leave.service.LeaveService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestValidatorImpl leaveRequestValidator;
    private final List<LeaveProcessor> processors;
    private final List<LeaveApproval> approvals;
    private final List<LeaveCanceller> cancellers;
    private final LeaveRequestCancelerImpl leaveRequestCanceler;

    @Autowired
    public LeaveServiceImpl(LeaveRequestRepository leaveRequestRepository, LeaveRepository leaveRepository, EmployeeRepository employeeRepository, LeaveRequestValidatorImpl leaveRequestValidator, List<LeaveProcessor> processors, List<LeaveApproval> approvals, List<LeaveCanceller> cancellers, LeaveRequestCancelerImpl leaveRequestCanceler) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
        this.leaveRequestValidator = leaveRequestValidator;
        this.processors = processors;
        this.approvals = approvals;
        this.cancellers = cancellers;
        this.leaveRequestCanceler = leaveRequestCanceler;
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
        // fetch the leave from db:
        Leave leave = leaveRepository.findByReferenceNumber(refNumber).orElseThrow();
        LeaveCanceller canceller =
                cancellers.stream().filter(c-> c.supports(leave.getLeaveType().toString()))
                .findFirst().orElse(null);
        log.info("Calling the canceller for leave type: "+ leave.getLeaveType().toString());
        assert canceller != null;
        canceller.cancel(refNumber);
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
    public Page<LeaveRequestResponse> getAllLeaveRequests(String email, int page, int size) {

        // Fetch the employee from db:
        Employee employee =
                employeeRepository.findByEmail(email).orElseThrow();

        Pageable pageable = PageRequest.of(page, size);

        Page<LeaveRequest> requests = leaveRequestRepository.findAllByEmployee(employee, pageable);

        return requests.map(LeaveRequestResponse::new);
    }

    @Override
    public Page<LeaveRequestResponse> getAllSubordinatesRequests(String email, int page, int size) {
        // Fetch the manager:
        Employee manager = employeeRepository
                .findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        // Fetch the subordinates
        List<Employee> subordinates = employeeRepository.findAllByManagerId(manager.getId());

        Pageable pageable = PageRequest.of(page, size);
        // Fetch leave requests (IN PROCESS ONLY):
        Page<LeaveRequest> requests = leaveRequestRepository
                .findByEmployeeInAndStatusAndApprovedByManager(
                        subordinates,
                        LeaveRequestStatus.IN_PROCESS,
                        false,
                        pageable
                );
        return requests.isEmpty() ? null : requests.map(LeaveRequestResponse::new);
    }

    @Override
    public Page<LeaveRequestResponse> getAllLeaveRequestsForHR(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        // Fetch all leave requests for HR:
        Page<LeaveRequest> requests =
                leaveRequestRepository.findAllByApprovedByManagerOrStatusOrStatus(
                        true,
                        LeaveRequestStatus.APPROVED,
                        LeaveRequestStatus.REJECTED,
                        pageable
                );

        // return a response dto:
        return
                requests.map(LeaveRequestResponse::new);
    }

    @Override
    public void approveSubordinateLeaveRequest(String approvedBy,Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow();
        LeaveApproval approval =
                approvals.stream().filter(p-> p.supports(leaveRequest.getTypeOfLeave().toString()))
                .findFirst().orElse(null);
        // approve:
        assert approval != null;
        approval.approveSubordinate(approvedBy,leaveRequest);
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
        approval.rejectSubordinate(rejectedBy ,leaveRequest);
    }

    @Override
    public void rejectLeaveRequest(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow();
        LeaveApproval approval =
                approvals.stream().filter(p-> p.supports(leaveRequest.getTypeOfLeave().toString()))
                        .findFirst().orElse(null);
        // approve:
        assert approval != null;
        approval.rejectLeave(leaveRequest);
    }
}
