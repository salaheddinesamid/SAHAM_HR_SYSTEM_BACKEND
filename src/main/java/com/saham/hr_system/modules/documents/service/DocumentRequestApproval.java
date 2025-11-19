package com.saham.hr_system.modules.documents.service;

/**
 * This interface defines the contract for approving or rejecting document requests.
 */
public interface DocumentRequestApproval {

    /**
     * This function handles the approval of document request
     * @param requestId
     */
    void approveDocumentRequest(Long requestId);
    /**
     * This function handles the rejection of document request
     * @param requestId
     */
    void rejectDocumentRequest(Long requestId);
}
