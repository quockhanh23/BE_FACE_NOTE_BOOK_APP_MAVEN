package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String status;
    @ManyToOne
    @JoinColumn(name = "send_to_id")
    private User idSendTo;
    @ManyToOne
    @JoinColumn(name = "action_id")
    private User idAction;
    private Long typeId;
    private String type;
    private Date createAt;
}
