package com.tony.cars.api.exeptions;

import com.tony.cars.domain.exceptions.DatabaseException;
import com.tony.cars.service.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Resource Not Found";
        HttpStatus status = HttpStatus.NOT_FOUND; //404

        StandardError myErr = new StandardError(
                Instant.now(),
                status.value(),
                error,
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(status).body(myErr);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> badRequestErr(DatabaseException e, HttpServletRequest request) {
        String error = "Database Error";
        HttpStatus status = HttpStatus.BAD_REQUEST; //400

        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                error, e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> argumentInvalidErr(IllegalArgumentException e, HttpServletRequest request) {
        String error = "Bad Request Error";
        HttpStatus status = HttpStatus.BAD_REQUEST; //400

        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                error, e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }
}

