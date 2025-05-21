package com.encoria.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleRequestBodyValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() == null
                                ? "Invalid value"
                                : fe.getDefaultMessage(),
                        (m1, m2) -> m1 + "; " + m2));

        return ErrorResponse.ofValidation(
                HttpStatus.BAD_REQUEST,
                "Request body validation failed",
                request.getRequestURI(),
                errors
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleServiceValidation(ConstraintViolationException ex, HttpServletRequest request) {
        var errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage)
                );

        return ErrorResponse.ofValidation(
                HttpStatus.BAD_REQUEST,
                "Request constraint violation",
                request.getRequestURI(),
                errors);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({PrivateProfileException.class, ResourceOwnershipException.class})
    public ErrorResponse handleForbidden(Exception ex, HttpServletRequest request) {
        return ErrorResponse.of(HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({UsernameAlreadyExistsException.class, UserProfileAlreadyExistsException.class, UserAlreadyFollowedException.class, FollowSelfException.class})
    public ErrorResponse handleConflict(RuntimeException ex, HttpServletRequest request) {
        return ErrorResponse.of(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class, MomentNotFoundException.class, CountryNotFoundException.class})
    public ErrorResponse handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return ErrorResponse.of(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleGeneric(RuntimeException ex, HttpServletRequest request) {
        log.error("Unhandled exception:", ex);
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI());
    }
}
