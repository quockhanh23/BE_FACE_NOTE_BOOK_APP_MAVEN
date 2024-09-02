package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class NameDTO {
    private Long id;
    private String fullName;
    private Date createAt;
    private String address;
    private String avatar;
}
