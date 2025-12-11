package com.saham.hr_system.modules.documents.service.implementation;

import com.saham.hr_system.modules.documents.model.DocumentRequest;
import com.saham.hr_system.modules.documents.model.DocumentRequestStatus;
import com.saham.hr_system.modules.documents.repository.DocumentRequestRepository;
import com.saham.hr_system.modules.documents.service.DocumentRequestApproval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class DocumentRequestApprovalImpl implements DocumentRequestApproval {

    private final DocumentRequestRepository documentRequestRepository;
    private final DocumentApprovalEmailSenderImpl documentApprovalEmailSender;

    @Autowired
    public DocumentRequestApprovalImpl(DocumentRequestRepository documentRequestRepository, DocumentApprovalEmailSenderImpl documentApprovalEmailSender) {
        this.documentRequestRepository = documentRequestRepository;
        this.documentApprovalEmailSender = documentApprovalEmailSender;
    }

    @Override
    public void approveDocumentRequest(Long requestId) {
        // fetch the request from dn:
        DocumentRequest documentRequest =
                documentRequestRepository.findById(requestId).orElse(null);

        // set the request status to approved:
        assert documentRequest != null;
        documentRequest.setStatus(DocumentRequestStatus.APPROVED);
        // save the request:
        documentRequestRepository.save(documentRequest);

        // notify the employee:
        CompletableFuture.runAsync(() -> {
            try {
                documentApprovalEmailSender.notifyEmployee(documentRequest);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void rejectDocumentRequest(Long requestId) {
        // fetch the request from dn:
        DocumentRequest documentRequest =
                documentRequestRepository.findById(requestId).orElse(null);

        // set the request status to approved:
        assert documentRequest != null;
        documentRequest.setStatus(DocumentRequestStatus.REJECTED);
        // save the request:
        documentRequestRepository.save(documentRequest);
    }
}
