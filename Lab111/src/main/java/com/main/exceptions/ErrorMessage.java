package com.main.exceptions;

import java.time.LocalDateTime;
import java.util.Date;

public class ErrorMessage {
    private String message;
    private LocalDateTime timestamp;


    public ErrorMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
