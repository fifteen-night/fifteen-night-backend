package com.fn.global.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice(annotations = RestController.class)
@Hidden
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity forbiddenException(HttpServletRequest req, ForbiddenException e) {
        ErrorResponse errorResponse = new ErrorResponse(req, HttpStatus.FORBIDDEN, e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity notFoundException(HttpServletRequest req, NotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(req, HttpStatus.NOT_FOUND, e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity conflictException(HttpServletRequest req, ConflictException e) {
        ErrorResponse errorResponse = new ErrorResponse(req, HttpStatus.FORBIDDEN, e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity unauthorizedException(HttpServletRequest req, UnauthorizedException e) {
        ErrorResponse errorResponse = new ErrorResponse(req, HttpStatus.UNAUTHORIZED, e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity badRequestException(HttpServletRequest req, BadRequestException e) {
        ErrorResponse errorResponse = new ErrorResponse(req, HttpStatus.BAD_REQUEST, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    /**
     * 400 Bad Request - DTO 필드 검증 실패 예외 처리 (Spring Boot 3.x / Spring Framework 6 대응)
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
        WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }


}
