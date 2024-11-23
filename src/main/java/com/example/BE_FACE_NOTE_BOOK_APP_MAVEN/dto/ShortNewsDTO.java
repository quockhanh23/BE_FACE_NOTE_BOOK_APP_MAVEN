package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ShortNewsDTO {
    private Long id;
    private String content;
    private Date createdAt;
    private String image;
    private String status;
    private UserDTO userDTO;
}
