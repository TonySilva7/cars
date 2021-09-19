package com.tony.cars.api.exeptionhandler;

import com.tony.cars.domain.exceptions.DomainException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // utilitária
    private ResponseEntity<Object> makeProblem(
        RuntimeException ex, WebRequest request, HttpServletRequest req, HttpStatus status) {

        Problem problem = new Problem();
        problem.setStatus(status.value());
        problem.setTitle(ex.getMessage());
        problem.setTimestamp(OffsetDateTime.now());
        problem.setPath(req.getRequestURI());

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    // trata erros lançados de autenticação (403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request, HttpServletRequest req) {

        HttpStatus status = HttpStatus.FORBIDDEN;

        return makeProblem(ex, request, req, status);
    }

    // trata erro lançados pela DomainException
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Object> handleDomainException(DomainException ex, WebRequest request, HttpServletRequest req) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return makeProblem(ex, request, req, status);
    }

    // trata vários erros
    @ExceptionHandler({
            EntityNotFoundException.class,
            IllegalArgumentException.class,
            NoSuchElementException.class
    })
    public ResponseEntity<Object> handleRequestException(RuntimeException ex, WebRequest request, HttpServletRequest req) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        if (ex instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
        }

        return makeProblem(ex, request, req, status);
    }

    // ................ ALGA WORKS ....................
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<FieldException> fieldExceptionList = new ArrayList<FieldException>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String errorName = ((FieldError) error).getField();
            String errorMessage = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            fieldExceptionList.add(new FieldException(errorName, errorMessage));
        }

        Problem problem = new Problem();
        problem.setStatus(status.value());
        problem.setTitle("Um ou mais campos estão incorretos.");
        problem.setTimestamp(OffsetDateTime.now());
        problem.setFieldExceptions(fieldExceptionList);

        return super.handleExceptionInternal(ex, problem, headers, status, request);
    }
}

