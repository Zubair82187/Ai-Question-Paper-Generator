package com.ai_question_paper_generator.exception;

import com.ai_question_paper_generator.model.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundException(NotFoundException notFoundException){
        return responseEntity(HttpStatus.NOT_FOUND, notFoundException);
    }

    @ExceptionHandler(FileNotSavedException.class)
    public ResponseEntity<Object> fileNotSavedException(FileNotSavedException exception){
        return responseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    @ExceptionHandler(FileNotAllowedException.class)
    public ResponseEntity<Object> fileNotAllowedException(FileNotAllowedException exception){
        return responseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Object> handleMissingPart(MissingServletRequestPartException ex){

        return responseEntity(
                HttpStatus.BAD_REQUEST,
                new Exception(ex.getRequestPartName() + " is required")
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex){

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult()
                .getFieldErrors()) {
            errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return responseEntity(HttpStatus.BAD_REQUEST, "Validation Failed", errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("json", "Malformed JSON or invalid structure");
        return responseEntity(HttpStatus.BAD_REQUEST, "Invalid request body", errors);
    }

    @ExceptionHandler(CouldNotCreated.class)
    public ResponseEntity<Object> couldNotCreated(CouldNotCreated ex){
        return responseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler
    public ResponseEntity<Object> NoResponseFound(NoResponseFound ex){
        return responseEntity(HttpStatus.NOT_FOUND, ex);
    }

    private ResponseEntity<Object> responseEntity(HttpStatus status, Exception exception){
        ResponseError responseError = new ResponseError(LocalDateTime.now(), exception.getMessage(), status.value());
        return new ResponseEntity<>(responseError, status);
    }

    private ResponseEntity<Object> responseEntity(HttpStatus status, String message, Map<String, String> errors){
        ResponseError responseError = new ResponseError(
                LocalDateTime.now(),
                message,
                status.value(),
                errors
        );
        return new ResponseEntity<>(responseError, status);
    }
}
