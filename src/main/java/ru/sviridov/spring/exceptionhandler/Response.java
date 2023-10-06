package ru.sviridov.spring.exceptionhandler;

import java.time.LocalDateTime;

public class Response {

    private final String errorMessage;

    private final LocalDateTime timestamp;
    private final String status;
    public Response(String errorMessage, LocalDateTime timestamp, String status) {
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


    public String getErrorMessage() {
        return errorMessage;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }


}
