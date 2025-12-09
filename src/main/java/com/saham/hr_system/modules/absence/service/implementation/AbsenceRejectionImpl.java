package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.exception.UnauthorizedAccessException;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.model.AbsenceRequestStatus;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.absence.service.AbsenceRejection;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class AbsenceRejectionImpl implements AbsenceRejection {
    private final AbsenceRequestRepo absenceRequestRepo;
    private final EmployeeRepository employeeRepository;
    private final AbsenceRequestRejectionEmailSenderImpl absenceRequestRejectionEmailSender;
    private final AbsenceRejectionEmailSenderImpl absenceRejectionEmailSender;

    @Autowired
    public AbsenceRejectionImpl(AbsenceRequestRepo absenceRequestRepo, EmployeeRepository employeeRepository, AbsenceRequestRejectionEmailSenderImpl absenceRequestRejectionEmailSender, AbsenceRejectionEmailSenderImpl absenceRejectionEmailSender) {
        this.absenceRequestRepo = absenceRequestRepo;
        this.employeeRepository = employeeRepository;
        this.absenceRequestRejectionEmailSender = absenceRequestRejectionEmailSender;
        this.absenceRejectionEmailSender = absenceRejectionEmailSender;
    }

    @Override
    public void rejectSubordinate(String email, String refNumber) {
        // fetch the absence request by refNumber and employee email
        AbsenceRequest absenceRequest =
                absenceRequestRepo.findByReferenceNumber(refNumber).orElseThrow();

        // fetch the manager:
        Employee manager =
                employeeRepository.findByEmail(email).orElseThrow();

        // check if the manager is indeed the manager of the employee who made the request
        if(!absenceRequest.getEmployee().getManager().getId().equals(manager.getId())) {
            throw new UnauthorizedAccessException("You are not authorized to reject this absence request.");
        }

        // check if the request is already approved or rejected
        if(absenceRequest.getStatus().toString().equals("REJECTED")) {
            throw new IllegalStateException("Absence request is already rejected.");
        }
        if(absenceRequest.getStatus().toString().equals("APPROVED")) {
            throw new IllegalStateException("Absence request is already approved.");
        }

        // otherwise:
        absenceRequest.setApprovedByManager(false);
        absenceRequest.setStatus(AbsenceRequestStatus.REJECTED);

        // save the absence request:
        absenceRequestRepo.save(absenceRequest);

        // notify the employee (omitted for brevity)
        CompletableFuture.runAsync(()->{
            try{
                absenceRequestRejectionEmailSender.notifyEmployee(absenceRequest);
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void rejectAbsence(String refNumber) {

        // fetch the request:
        AbsenceRequest absenceRequest =
                absenceRequestRepo.findByReferenceNumber(refNumber).orElseThrow();
        // check if the request is already approved by the manager:
        if(!absenceRequest.isApprovedByManager()){
            throw new IllegalStateException("Absence request is not yet approved by the manager.");
        }
        // check if the request is already rejected:
        if(absenceRequest.getStatus().equals(AbsenceRequestStatus.REJECTED)){
            throw new IllegalStateException("Absence request is already rejected.");
        }
        // check if the request is already approved:
        if(absenceRequest.getStatus().equals(AbsenceRequestStatus.APPROVED)){
            throw new IllegalStateException("Absence request is already rejected.");
        }

        absenceRequest.setApprovedByHr(false);
        absenceRequest.setStatus(AbsenceRequestStatus.REJECTED);

        // save the request:
        absenceRequestRepo.save(absenceRequest);

        // notify the employee and manager:
        CompletableFuture.runAsync(()->{
            try{
                absenceRejectionEmailSender.notifyEmployee(absenceRequest);
                absenceRejectionEmailSender.notifyManager(absenceRequest);
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        });
    }
}
