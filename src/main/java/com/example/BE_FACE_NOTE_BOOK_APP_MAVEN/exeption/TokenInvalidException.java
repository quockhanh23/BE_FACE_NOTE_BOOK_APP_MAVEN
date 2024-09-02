package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.exeption;

public class TokenInvalidException extends RuntimeException {

    public TokenInvalidException(String msg) {
        super(msg);
    }
}
