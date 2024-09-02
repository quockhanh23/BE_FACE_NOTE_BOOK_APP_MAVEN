package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.exeption;

import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidException extends RuntimeException {
    private Map<String, String> items;

    public InvalidException(String msg) {
        super(msg);
    }

    public InvalidException(String msg, Map<String, String> fieldError) {
        super(msg);
        items = fieldError;
    }
}
