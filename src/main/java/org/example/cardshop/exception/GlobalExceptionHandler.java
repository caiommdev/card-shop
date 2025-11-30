package org.example.cardshop.exception;

import org.example.cardshop.dto.ErrorResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * Handles validation errors and type conversion errors.
 * Returns JSON for API requests and HTML for browser requests.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions from @Valid annotation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse("Validation failed", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        // For browser requests, store errors in model and return error page
        return handleBrowserValidationError(errors);
    }

    /**
     * Handles custom validation exceptions
     */
    @ExceptionHandler(ValidationException.class)
    public Object handleValidationException(ValidationException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        return handleBrowserValidationError(ex.getErrors());
    }

    /**
     * Handles type mismatch exceptions (e.g., non-numeric price input)
     */
    @ExceptionHandler({TypeMismatchException.class, MethodArgumentTypeMismatchException.class})
    public Object handleTypeMismatch(Exception ex, HttpServletRequest request, Model model) {
        String errorMessage = "Invalid input: Please ensure all fields are filled correctly. Price must be a valid number.";

        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        model.addAttribute("error", errorMessage);
        return "error";
    }

    /**
     * Handles binding exceptions (validation errors during data binding)
     */
    @ExceptionHandler(BindException.class)
    public Object handleBindException(BindException ex, HttpServletRequest request, Model model) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse("Validation failed", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        return handleBrowserValidationError(errors);
    }

    /**
     * Handles illegal argument exceptions (e.g., invalid card ID)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    /**
     * Handles any other unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public Object handleGenericException(Exception ex, HttpServletRequest request, Model model) {
        String errorMessage = "An unexpected error occurred: " + ex.getMessage();

        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        model.addAttribute("error", errorMessage);
        return "error";
    }

    /**
     * Check if the request is from an API client (wants JSON response)
     */
    private boolean isApiRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String contentType = request.getHeader("Content-Type");

        return (accept != null && accept.contains("application/json")) ||
               (contentType != null && contentType.contains("application/json")) ||
               request.getRequestURI().startsWith("/api/");
    }

    /**
     * Handle validation errors for browser requests
     */
    private String handleBrowserValidationError(Map<String, String> errors) {
        // For now, we'll format errors as a single message
        // The controller will handle showing errors properly on the form
        StringBuilder errorMessage = new StringBuilder("Validation failed:\n");
        errors.forEach((field, message) ->
            errorMessage.append("- ").append(field).append(": ").append(message).append("\n")
        );

        org.springframework.ui.Model model = new org.springframework.ui.ExtendedModelMap();
        model.addAttribute("error", errorMessage.toString());
        return "error";
    }
}

