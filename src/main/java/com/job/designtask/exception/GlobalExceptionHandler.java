package com.job.designtask.exception;

import com.job.designtask.model.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderProcessingException.class)
    public ResponseEntity<ErrorMessage> handleDoctorNotFoundException(OrderProcessingException e, HttpServletRequest r) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .code(NOT_FOUND.value())
                .status(NOT_FOUND.getReasonPhrase())
                .message(e.getMessage())
                .uri(r.getRequestURI())
                .method(r.getMethod())
                .build(), NOT_FOUND);
    }

    @ExceptionHandler(OrderUpdateError.class)
    public ResponseEntity<ErrorMessage> handleDoctorNotFoundException(OrderUpdateError e, HttpServletRequest r) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .code(NOT_FOUND.value())
                .status(NOT_FOUND.getReasonPhrase())
                .message(e.getMessage())
                .uri(r.getRequestURI())
                .method(r.getMethod())
                .build(), NOT_FOUND);
    }

//    @ExceptionHandler(OrderProcessingException.class)
//    public ResponseEntity<ApiResponse<Void>> handleOrderProcessingException(OrderProcessingException ex) {
//        System.out.println("OrderProcessingException");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(ApiResponse.error(ex.getMessage()));
//    }
//
//    @ExceptionHandler(OrderUpdateError.class)
//    public ResponseEntity<ApiResponse<Void>> handleOrderProcessingException(OrderUpdateError ex) {
//        System.out.println("orderUpdateError");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(ApiResponse.error(ex.getMessage()));
//    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        List<String> errorMessages = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(this::formatFieldError)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.badRequest()
//                .body(ApiResponse.error(String.join("; ", errorMessages)));
//    }
//
//    private String formatFieldError(FieldError fieldError) {
//        return String.format("Field '%s': %s (Rejected value: '%s')",
//                fieldError.getField(),
//                fieldError.getDefaultMessage(),
//                fieldError.getRejectedValue());
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ApiResponse.error("An unexpected error occurred: " + ex.getMessage()));
//    }
}