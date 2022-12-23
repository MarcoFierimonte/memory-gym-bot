package com.f90.telegram.bot.memorygymbot.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(Exception e, WebRequest request) {
        LOGGER.warn("exceptionHandler() - OUT: exception message=[{}]", e.getMessage(), e);
        String errorMessage;
        HttpStatus status;
        Map<String, Object> body = new LinkedHashMap<>();

        if (e instanceof MethodArgumentNotValidException) {
            status = HttpStatus.BAD_REQUEST;
            errorMessage = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
        } else if (e instanceof ServletRequestBindingException || e instanceof MissingServletRequestPartException
                || e instanceof TypeMismatchException || e instanceof HttpMessageNotReadableException) {
            status = HttpStatus.BAD_REQUEST;
            errorMessage = e.getMessage();
        } else if (e instanceof ConstraintViolationException) {
            status = HttpStatus.BAD_REQUEST;
            errorMessage = ((ConstraintViolationException) e).getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            status = HttpStatus.METHOD_NOT_ALLOWED;
            errorMessage = e.getMessage();
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
            errorMessage = e.getMessage();
        } else if (e instanceof HttpMediaTypeNotAcceptableException) {
            status = HttpStatus.NOT_ACCEPTABLE;
            errorMessage = e.getMessage();
        } else if (e instanceof AsyncRequestTimeoutException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            errorMessage = e.getMessage();
        } else if (e instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) e;
            status = responseStatusException.getStatus();
            errorMessage = responseStatusException.getMessage();
        } else if (e.getClass().getAnnotation(ResponseStatus.class) != null) {
            ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
            status = responseStatus.code();
            errorMessage = e.getMessage();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorMessage = e.getMessage();
        }

        body.put("timestamp", Instant.now());
        body.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", errorMessage);

        return new ResponseEntity<>(body, status);
    }

}