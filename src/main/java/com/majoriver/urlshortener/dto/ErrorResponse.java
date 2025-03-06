package com.majoriver.urlshortener.dto;

public class ErrorResponse {
    private String message;
    private Object details;

    public ErrorResponse(String message, Object details) {
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
