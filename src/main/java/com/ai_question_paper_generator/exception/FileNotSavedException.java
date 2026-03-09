package com.ai_question_paper_generator.exception;

public class FileNotSavedException extends RuntimeException{
    public FileNotSavedException(String message){
        super(message);
    }
}
