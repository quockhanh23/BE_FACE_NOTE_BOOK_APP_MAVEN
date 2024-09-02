package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MessengerDTO {
    private Long id;
    private String content;
    private String statusMessenger;
    private Date createAt;
    private Long idConversation;
}
