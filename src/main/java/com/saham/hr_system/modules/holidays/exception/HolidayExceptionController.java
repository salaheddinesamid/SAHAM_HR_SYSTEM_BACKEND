package com.saham.hr_system.modules.holidays.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class HolidayExceptionController {

    @ExceptionHandler(HolidayNotFoundException.class)
    public ResponseEntity<?> handleHolidayNotFound(){
        return ResponseEntity.status(200)
                .body(Map.of("message", "Holiday not found"));
    }
}
