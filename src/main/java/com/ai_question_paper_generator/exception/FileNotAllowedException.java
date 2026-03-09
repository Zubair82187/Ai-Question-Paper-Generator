package com.ai_question_paper_generator.exception;

public class FileNotAllowedException extends RuntimeException{
    public FileNotAllowedException(String message){
        super(message);
    }
}
