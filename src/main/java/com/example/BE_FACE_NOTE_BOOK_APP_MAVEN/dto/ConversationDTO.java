package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ConversationDTO {
    private Long id;
    private Date createAt;
    private String statusConversation;
    private UserDTO idSender;
    private UserDTO idReceiver;
}
