package ru.sviridov.spring.exceptionhandler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Response> handleException (NoSuchElementException e) {
        Response response = new Response(e.getMessage(), LocalDateTime.now(), String.valueOf(HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response> handleDataIntegrityException(DataIntegrityViolationException e) {
        Response response = new Response(e.getMessage() + " (try 1 more time)", LocalDateTime.now(), String.valueOf(HttpStatus.CONFLICT.value()));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}
