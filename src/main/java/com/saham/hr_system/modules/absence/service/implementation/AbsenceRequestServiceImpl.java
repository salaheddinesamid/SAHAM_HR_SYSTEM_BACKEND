package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.exception.AbsenceRequestNotFoundException;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.absence.service.AbsenceApproval;
import com.saham.hr_system.modules.absence.service.AbsenceRequestProcessor;
import com.saham.hr_system.modules.absence.service.AbsenceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link AbsenceRequestService}.
 * <p>
 * This service manages the lifecycle of absence requests, including creating
 * new requests, approving them by managers, and final HR approval. It delegates
 * processing to specific {@link AbsenceRequestProcessor} implementations
 * and approvals to {@link AbsenceApproval} implementations based on absence type.
 */
@Service
public class AbsenceRequestServiceImpl implements AbsenceRequestService {

    private final List<AbsenceRequestProcessor> processors;
    private final List<AbsenceApproval> approvals;
    private final AbsenceRequestRepo absenceRequestRepo;

    @Autowired
    public AbsenceRequestServiceImpl(List<AbsenceRequestProcessor> processors, List<AbsenceApproval> approvals, AbsenceRequestRepo absenceRequestRepo) {
        this.processors = processors;
        this.approvals = approvals;
        this.absenceRequestRepo = absenceRequestRepo;
    }

    @Override
    public AbsenceRequestDetails requestAbsence(String email, AbsenceRequestDto requestDto) throws Exception {
        AbsenceRequestProcessor processor =
                processors.stream().filter(p -> p.supports(requestDto.getType()))
                        .findFirst().orElse(null);

        assert processor != null;
        AbsenceRequest absence = processor.processAbsenceRequest(email, requestDto);

        return new AbsenceRequestDetails(absence);
    }

    @Override
    public void approveAbsenceRequest(String approvedBy, String refNumber) throws Exception {
        try {
            AbsenceRequest absenceRequest = absenceRequestRepo.findByReferenceNumber(refNumber)
                    .orElseThrow(() -> new AbsenceRequestNotFoundException("Absence request with reference number " + refNumber + " not found."));

            AbsenceApproval approval =
                    approvals.stream().filter(a -> a.supports(absenceRequest.getType().toString()))
                            .findFirst().orElse(null);

            assert approval != null;
            approval.approveSubordinate(approvedBy, absenceRequest);
        } catch (AbsenceRequestNotFoundException e) {
            throw new AbsenceRequestNotFoundException("Absence request with reference number " + refNumber + " not found.");
        }
    }

    @Override
    public void approveAbsence(String refNumber) throws Exception {
        try {
            AbsenceRequest absenceRequest = absenceRequestRepo.findByReferenceNumber(refNumber)
                    .orElseThrow(() -> new AbsenceRequestNotFoundException("Absence request with reference number " + refNumber + " not found."));

            AbsenceApproval approval =
                    approvals.stream().filter(a -> a.supports(absenceRequest.getType().toString()))
                            .findFirst().orElse(null);

            assert approval != null;
            approval.approve(absenceRequest);
        } catch (AbsenceRequestNotFoundException e) {
            throw new AbsenceRequestNotFoundException("Absence request with reference number " + refNumber + " not found.");
        }
    }
}
