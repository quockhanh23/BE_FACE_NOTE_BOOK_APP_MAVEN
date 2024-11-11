package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.exeption;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String msg) {
        super(msg);
    }
}
