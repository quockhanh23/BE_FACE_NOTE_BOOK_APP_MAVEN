package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendRelationDTO {
    private Long id;
    private Long idUser;
    private Long idFriend;
    private String statusFriend;
    private UserDTO friend;
    private UserDTO userLogin;
}
