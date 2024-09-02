package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // Phải có
// Nếu để @NoArgsConstructor thì không chạy được
public class MessengerDTO2 {
    private Long id;
    private String content;
}
