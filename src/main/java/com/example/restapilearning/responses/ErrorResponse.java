package com.example.restapilearning.responses;

public class ErrorResponse {

    String message;
    String description;

    public ErrorResponse(String message, String description) {
        this.message = message;
        this.description = description;
    }


}
