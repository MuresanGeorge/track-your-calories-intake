package com.george.tracker.exception.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApiError {

    private int status;

    private String error;

    private LocalDateTime timestamp;

    private String message;

    private List<String> errors;

    public ApiError(int status, String error, LocalDateTime timestamp, String message) {
        this.status = status;
        this.error = error;
        this.timestamp = timestamp;
        this.message = message;
    }

    public ApiError(int status, List<String> errors, LocalDateTime timestamp, String message) {
        this.status = status;
        this.errors = errors;
        this.timestamp = timestamp;
        this.message = message;
    }
}
