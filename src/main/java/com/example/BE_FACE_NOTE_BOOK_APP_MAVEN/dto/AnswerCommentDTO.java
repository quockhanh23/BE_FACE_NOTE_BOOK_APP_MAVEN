package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class AnswerCommentDTO {
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime editAt;
    private String image;
    private CommentDTO commentDTO;
    private UserDTO userDTO;
}
