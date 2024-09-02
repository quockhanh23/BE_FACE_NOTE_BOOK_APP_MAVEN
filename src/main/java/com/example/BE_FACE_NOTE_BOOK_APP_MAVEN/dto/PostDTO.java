package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private Date createAt;
    private Date editAt;
    private String content;
    private Long numberLike;
    private Long numberDisLike;
    private String image;
    private Long iconHeart;
    private String status;
    private UserDTO userDTO;
    private long countAllComment;
}
