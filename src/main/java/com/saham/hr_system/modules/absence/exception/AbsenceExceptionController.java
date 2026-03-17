package com.saham.hr_system.modules.absence.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
/** * AbsenceExceptionController is responsible for handling exceptions related to absence requests and medical certificates.
 * It provides methods to handle specific exceptions and return appropriate HTTP responses.
 */
@Controller
public class AbsenceExceptionController {

    /**
     * Handles AbsenceRequestNotFoundException and returns a 404 Not Found response with the exception message.
     *
     * @param ex the AbsenceRequestNotFoundException to handle
     * @return a ResponseEntity containing the error message and HTTP status code
     */
    @ExceptionHandler(AbsenceRequestNotFoundException.class)
    public ResponseEntity<?> handleAbsenceRequestNotFound(AbsenceRequestNotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(ex.getMessage());
    }

    /**
     * Handles MedicalCerificateNotFoundException and returns a 404 Not Found response with a message indicating the missing medical certificate.
     *
     * @param ex the MedicalCerificateNotFoundException to handle
     * @return a ResponseEntity containing the error message and HTTP status code
     */
    @ExceptionHandler(MedicalCerificateNotFoundException.class)
    public ResponseEntity<?> handleMedicalCertificateNotFound(MedicalCerificateNotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body("Medical Certificate not found with path: " + ex.getMessage());
    }
}
