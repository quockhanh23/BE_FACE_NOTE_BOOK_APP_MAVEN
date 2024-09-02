package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
// Không đặt tên là post được
public class Post2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createAt;
    private Date editAt;
    @Column(nullable = false, length = 1000)
    private String content;
    private String status;
    private boolean isDelete;
    private Long numberLike;
    private Long numberDisLike;
    @Column(length = 1000)
    private String image;
    private Long iconHeart;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

