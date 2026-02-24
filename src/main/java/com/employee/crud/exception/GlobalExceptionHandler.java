package com.employee.crud.exception;

import com.employee.crud.dto.ErrorResponse;
import com.employee.crud.entity.User;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
//@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {

        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUser(DuplicateUserException due) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                due.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                LocalDateTime.now()
        ), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid credentials",
                LocalDateTime.now()
        ), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtExpired(io.jsonwebtoken.ExpiredJwtException ex) {
        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "JWT token expired",
                LocalDateTime.now()
        ), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(io.jsonwebtoken.JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(io.jsonwebtoken.JwtException ex) {
        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid JWT token",
                LocalDateTime.now()
        ), HttpStatus.UNAUTHORIZED);
    }
}