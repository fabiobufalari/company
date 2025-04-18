package com.bufalari.company.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.bufalari.company.exception.CompanyNotFoundException;
import org.springframework.context.MessageSource;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    // Injeta MessageSource para mensagens de erro internacionalizadas / Injects MessageSource for internationalized error messages

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<String> handleCompanyNotFoundException(CompanyNotFoundException ex, Locale locale) {
        String errorMessage = messageSource.getMessage("error.company.notFound", null, locale);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
    // Lida com exceção de empresa não encontrada / Handles company not found exception (404 Not Found)

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e, Locale locale) {
        String errorPrefix = messageSource.getMessage("error.generic", null, locale);
        return new ResponseEntity<>(errorPrefix + ": " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
