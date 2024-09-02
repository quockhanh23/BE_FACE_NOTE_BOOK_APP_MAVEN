package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class UserBlackListDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
    private String cover;
    private String gender;
    private LocalDate dateOfBirt;
}
