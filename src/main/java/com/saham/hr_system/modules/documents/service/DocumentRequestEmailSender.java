package com.saham.hr_system.modules.documents.service;

import com.saham.hr_system.modules.documents.model.DocumentRequest;
import jakarta.mail.MessagingException;

public interface DocumentRequestEmailSender {
    /**
     *
     * @param documentRequest
     * @throws MessagingException
     */
    void notifyEmployee(DocumentRequest documentRequest) throws MessagingException;

    /**
     *
     * @param documentRequest
     * @throws MessagingException
     */
    void notifyHR(DocumentRequest documentRequest) throws MessagingException;
}
