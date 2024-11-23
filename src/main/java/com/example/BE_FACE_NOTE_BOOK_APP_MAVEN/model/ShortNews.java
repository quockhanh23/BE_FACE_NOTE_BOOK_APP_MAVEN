package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.ShortNewsExpiredStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShortNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 120)
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private Date expirationDate;
    private ShortNewsExpiredStatus expired;
    @Column(length = 1000)
    private String image;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private boolean isDelete;
    private String status;
}
