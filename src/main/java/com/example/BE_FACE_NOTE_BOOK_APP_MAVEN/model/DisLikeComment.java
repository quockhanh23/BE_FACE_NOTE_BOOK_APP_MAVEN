package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DisLikeComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userDisLike;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
