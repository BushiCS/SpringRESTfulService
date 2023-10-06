package ru.sviridov.spring.exceptionhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExceptionAdviceTest {
    @InjectMocks
    private ExceptionAdvice exceptionAdvice;

    @Mock
    private NoSuchElementException noSuchElementException;

    @Mock
    DataIntegrityViolationException dataIntegrityViolationException;


    @Test
    void handleException() {
        String message = "Element not found";

        when(noSuchElementException.getMessage()).thenReturn(message);

        ResponseEntity<Response> responseEntity = exceptionAdvice.handleException(noSuchElementException);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(message, Objects.requireNonNull(responseEntity.getBody()).getErrorMessage());
        assertEquals(LocalDateTime.now().getHour(), Objects.requireNonNull(responseEntity.getBody()).getTimestamp().getHour());
        assertEquals(String.valueOf(HttpStatus.NOT_FOUND.value()), Objects.requireNonNull(responseEntity.getBody()).getStatus());
    }

    @Test
    void handleDataIntegrityException() {
        String message = "could not execute statement; SQL [n/a]; constraint [null];";
        when(dataIntegrityViolationException.getMessage()).thenReturn(message);

        ResponseEntity<Response> responseEntity = exceptionAdvice.handleDataIntegrityException(dataIntegrityViolationException);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals(message + " (try 1 more time)", Objects.requireNonNull(responseEntity.getBody()).getErrorMessage());
        assertEquals(LocalDateTime.now().getHour(), Objects.requireNonNull(responseEntity.getBody()).getTimestamp().getHour());
        assertEquals(String.valueOf(HttpStatus.CONFLICT.value()), Objects.requireNonNull(responseEntity.getBody()).getStatus());
    }
}