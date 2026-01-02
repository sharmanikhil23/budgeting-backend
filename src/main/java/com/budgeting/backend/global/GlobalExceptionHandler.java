package com.budgeting.backend.global;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - Validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        return buildErrorResponse("Input validation failed", fieldErrors, HttpStatus.BAD_REQUEST);
    }

    // 400 - Illegal arguments (business validation)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex) {

        // Example: split message by commas for multiple errors
        String[] errorsArray = ex.getMessage().split(",");
        Map<String, String> fieldErrors = new HashMap<>();
        for (int i = 0; i < errorsArray.length; i++) {
            fieldErrors.put("error" + (i + 1), errorsArray[i].trim());
        }

        return buildErrorResponse("Input validation failed", fieldErrors, HttpStatus.BAD_REQUEST);
    }

    // 409 - MongoDB unique constraint violations
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateKey(DuplicateKeyException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("email", "This email is already registered");

        return buildErrorResponse("Duplicate resource", errors, HttpStatus.CONFLICT);
    }

    // 500 - Catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        return buildErrorResponse("Internal server error", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Helper to keep response consistent
    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            String message,
            Map<String, String> errors,
            HttpStatus status) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        response.put("errors", errors);

        return ResponseEntity.status(status).body(response);
    }
}
