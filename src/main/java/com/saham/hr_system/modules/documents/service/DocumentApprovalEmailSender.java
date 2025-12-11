package com.saham.hr_system.modules.documents.service;

import com.saham.hr_system.modules.documents.model.DocumentRequest;
import jakarta.mail.MessagingException;

public interface DocumentApprovalEmailSender {
    /**
     *
     * @param documentRequest
     * @throws MessagingException
     */
    void notifyEmployee(DocumentRequest documentRequest) throws MessagingException;
}
