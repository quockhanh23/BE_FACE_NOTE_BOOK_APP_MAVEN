package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.exeption;

public class RequestLimitException extends RuntimeException {
    public RequestLimitException(String msg) {
        super(msg);
    }
}
