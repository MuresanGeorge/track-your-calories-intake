package com.george.tracker.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.george.tracker.exception.ConsumptionNotFoundException;
import com.george.tracker.exception.IngredientDuplicateException;
import com.george.tracker.exception.IngredientNotFoundException;
import com.george.tracker.exception.IngredientStockNotFoundException;
import com.george.tracker.exception.RecipeNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IngredientNotFoundException.class})
    public ResponseEntity<ApiError> handleIngredientNotFound(Exception ex) {
        ApiError apiError = new ApiError();
        apiError.setError(ex.getMessage());
        apiError.setMessage("Provide the correct data to find the ingredient");
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        apiError.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ConsumptionNotFoundException.class})
    public ResponseEntity<ApiError> handleConsumptionNotFound(Exception ex) {
        ApiError apiError = new ApiError();
        apiError.setError(ex.getMessage());
        apiError.setMessage("Provide the correct data to find the consumption");
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        apiError.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({RecipeNotFoundException.class})
    public ResponseEntity<ApiError> handleRecipeNotFound(Exception ex) {
        ApiError apiError = new ApiError();
        apiError.setError(ex.getMessage());
        apiError.setMessage("Provide the correct data to find the recipe");
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        apiError.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IngredientStockNotFoundException.class})
    public ResponseEntity<ApiError> handleIngredientStockNotFound(Exception ex) {
        ApiError apiError = new ApiError();
        apiError.setError(ex.getMessage());
        apiError.setMessage("Provide the correct data to find the ingredient stock");
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        apiError.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IngredientDuplicateException.class})
    public ResponseEntity<ApiError> handleIngredientDuplicateException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), ex.getMessage(), LocalDateTime.now(),
                "The ingredient already exists");
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), errors, LocalDateTime.now(),
                "Provide a valid request body");
        return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        StringBuilder error = new StringBuilder();
        error.append(ex.getMethod());
        error.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> error.append(t + " "));

        ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED.value(), error.toString(), LocalDateTime.now(),
                "Select another HTTP verb");
        return handleExceptionInternal(ex, apiError, headers, HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String error = ex.getCause().getMessage();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), error, LocalDateTime.now(),
                "Change the value type for specified field");
        return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);

    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = ex.getName() + " should be " + ex.getRequiredType().getName();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), error, LocalDateTime.now(),
                "Provide the correct type ");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({JsonProcessingException.class})
    public ResponseEntity<ApiError> handleJsonProcessing(JsonProcessingException ex) {
        String error = ex.getCause().getMessage();
        ApiError apiError = new ApiError(HttpStatus.BAD_GATEWAY.value(), error, LocalDateTime.now(),
                "Cannot convert USDA ingredients to simple ingredients");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_GATEWAY);
    }
}
