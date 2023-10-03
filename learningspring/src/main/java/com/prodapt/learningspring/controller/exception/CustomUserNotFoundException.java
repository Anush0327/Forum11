package com.prodapt.learningspring.controller.exception;

public class CustomUserNotFoundException extends Exception{

    public CustomUserNotFoundException(String message){
        super(message);
    }
}
