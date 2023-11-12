package com.bpdev.roomservice.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class DefaultExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> Exception(Exception e, HttpServletRequest request) {
        logger.error("Exception occurred: ", e);
        String error = "Server error";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> MethodArgumentNotValidException
            (MethodArgumentNotValidException e, HttpServletRequest request) {
        logger.error("MethodArgumentNotValidException occurred: ", e);
        String error = "Invalid arguments";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        final List<String> errors = new ArrayList<>();
        for (final FieldError err : e.getBindingResult().getFieldErrors()) {
            errors.add(err.getField() + ": " + err.getDefaultMessage());
        }

        StandardError err = new StandardError(Instant.now(), status.value(), error,
                errors.toString(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> ResourceNotFoundException
            (ResourceNotFoundException e, HttpServletRequest request) {
        logger.error("ResourceNotFoundException occurred: ", e);
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ResponseEntity<StandardError> UniqueConstraintViolationException
            (UniqueConstraintViolationException e, HttpServletRequest request) {
        logger.error("UniqueConstraintViolationException occurred: ", e);
        String error = "Duplicate entry found. Please provide a unique value";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> InvalidFormatException
            (HttpMessageNotReadableException e, HttpServletRequest request) {
        logger.error("InvalidFormatException occurred: ", e);
        String error = "JSON parse error";

        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {
            if (Enum.class.isAssignableFrom(invalidFormatException.getTargetType())) {
                Class<? extends Enum> enumClass = (Class<? extends Enum>)
                        invalidFormatException.getTargetType();
                String invalidValue = invalidFormatException.getValue().toString();
                String validValues = Arrays.stream(enumClass.getEnumConstants())
                        .map(Enum::name)
                        .collect(Collectors.joining(", "));

                String message = String.format(
                        "%s is not one of the values accepted for Enum Class %s, valid values are: %s",
                        invalidValue, enumClass.getSimpleName(), validValues);
                HttpStatus status = HttpStatus.BAD_REQUEST;
                StandardError err = new StandardError(Instant.now(), status.value(), error,
                        message, request.getRequestURI());
                return ResponseEntity.status(status).body(err);
            }
        }

        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

}

