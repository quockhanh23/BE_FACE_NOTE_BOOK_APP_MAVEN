package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PostCheck {
    private Long id;
    private Date createAt;
    private Date editAt;
    private String content;
    private String image;
    private String fullName;
    private Long idUser;
}
