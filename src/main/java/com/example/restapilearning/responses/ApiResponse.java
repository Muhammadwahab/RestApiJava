package com.example.restapilearning.responses;

public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public ApiResponse(int status, String message, T object) {
        this.status=status;
        this.message=message;
        this.data=object;
    }

    // Constructors, getters, and setters

    // Static method to create a success response
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data);
    }

    // Static method to create an error response
    @org.jetbrains.annotations.NotNull
    @org.jetbrains.annotations.Contract(value = "_, _, _ -> new", pure = true)
    public static <T> ApiResponse<T> error(int status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }
}