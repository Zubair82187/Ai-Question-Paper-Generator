package com.ai_question_paper_generator.exception;

import com.ai_question_paper_generator.model.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
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

    private ResponseEntity<Object> responseEntity(HttpStatus status, Exception exception){
        ResponseError responseError = new ResponseError(LocalDateTime.now(), exception.getMessage(), status.value());
        return new ResponseEntity<>(exception, status);
    }
}
