package com.saham.hr_system.modules.absence.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public class AbsenceExceptionController {

    @ExceptionHandler(AbsenceRequestNotFoundException.class)
    public ResponseEntity<?> handleAbsenceRequestNotFound(AbsenceRequestNotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MedicalCerificateNotFoundException.class)
    public ResponseEntity<?> handleMedicalCertificateNotFound(MedicalCerificateNotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body("Medical Certificate not found with path: " + ex.getMessage());
    }
}
