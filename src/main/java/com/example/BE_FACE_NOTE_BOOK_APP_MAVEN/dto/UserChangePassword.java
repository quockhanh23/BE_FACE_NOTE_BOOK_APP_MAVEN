package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserChangePassword {
    private Long id;
    private String passwordOld;
    private String passwordNew;
    private String confirmPasswordNew;
}
