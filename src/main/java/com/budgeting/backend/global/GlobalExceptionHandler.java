package com.budgeting.backend.global;

import com.budgeting.backend.dto.out.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j // This provides the 'log' object automatically
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildErrorResponse("Input validation failed", details, HttpStatus.BAD_REQUEST);
    }

    // 400 - Illegal arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse("Invalid request", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 409 - Concurrency Conflict
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleOptimisticLocking(OptimisticLockingFailureException ex) {
        return buildErrorResponse("Conflict", "The record was updated by another user. Please refresh.", HttpStatus.CONFLICT);
    }

    // 409 - Duplicate Key
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleDuplicateKey(DuplicateKeyException ex) {
        return buildErrorResponse("Conflict", "A resource with this unique identifier already exists.", HttpStatus.CONFLICT);
    }

    // 500 - Catch-all (Hides internal details)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleGeneralException(Exception ex) {
        // 1. Log the full stack trace for the developer to see in the console/logs
        log.error("INTERNAL SERVER ERROR: ", ex);

        // 2. Send a generic, safe message to the client
        return buildErrorResponse(
                "Internal server error",
                "An unexpected error occurred. Please try again later or contact support if the issue persists.",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // Helper using your ApiResponseDTO
    private ResponseEntity<ApiResponseDTO<Object>> buildErrorResponse(
            String message,
            String errorDetails,
            HttpStatus status) {

        ApiResponseDTO<Object> response = new ApiResponseDTO<>(
                "error",         // status
                message,         // message
                new HashMap<>(), // data (empty object {})
                errorDetails     // errors (string details)
        );

        return new ResponseEntity<>(response, status);
    }
}