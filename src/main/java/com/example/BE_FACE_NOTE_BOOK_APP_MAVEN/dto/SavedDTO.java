package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class SavedDTO {
    private Long id;
    private Long idUser;
    private String status;
    private Date saveDate;
    private PostDTO postDTO;
}
