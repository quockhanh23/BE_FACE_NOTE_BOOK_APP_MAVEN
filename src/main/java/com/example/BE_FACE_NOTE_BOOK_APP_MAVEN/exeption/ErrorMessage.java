package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.exeption;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ErrorMessage {
    private int statusCode;
    private Date timestamp = new Date();
    private String message;
    private String description;
    private Map<String, String> fieldError;
}
