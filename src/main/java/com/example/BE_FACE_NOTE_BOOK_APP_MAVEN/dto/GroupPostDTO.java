package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class GroupPostDTO {
    private Long id;
    private String status;
    private String createBy;
    private Date createAt;
    private String image;
    private String content;
    private Long idUser;
    private String userName;
    private Long idTheGroup;
    private String groupName;
}
