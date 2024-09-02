package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailContent {
    public String email;
    public String username;
    public String password;
    public String title;
    public String content;
}
