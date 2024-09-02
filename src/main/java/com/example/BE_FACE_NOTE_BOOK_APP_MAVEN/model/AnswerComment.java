package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AnswerComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500, nullable = false)
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime editAt;
    private boolean isDelete;
    private LocalDateTime deleteAt;
    @Column(length = 1000)
    private String image;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
