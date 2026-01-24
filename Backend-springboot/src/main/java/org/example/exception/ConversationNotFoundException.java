package org.example.exception;

public class ConversationNotFoundException extends RuntimeException {
    public ConversationNotFoundException(String message){
        super(message);
    }
}
