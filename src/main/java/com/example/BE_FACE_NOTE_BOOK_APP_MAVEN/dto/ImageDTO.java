package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ImageDTO {
    private Long id;
    private String linkImage;
    private String status;
    private Date createAt;
    private Date deleteAt;
}
