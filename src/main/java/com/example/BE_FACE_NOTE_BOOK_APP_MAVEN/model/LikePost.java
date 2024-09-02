package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class LikePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userLike;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post2 post;
}
