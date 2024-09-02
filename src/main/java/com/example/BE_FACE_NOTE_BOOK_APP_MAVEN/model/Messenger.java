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
public class Messenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 300)
    private String content;
    @Column(length = 1000)
    private String image;
    private Date createAt;
    private String format;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User idSender;
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
    @ManyToOne
    @JoinColumn(name = "conversationDeleteTime_id")
    private ConversationDeleteTime conversationDeleteTime;
}
