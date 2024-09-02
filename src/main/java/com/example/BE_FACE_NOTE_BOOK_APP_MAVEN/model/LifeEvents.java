package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class LifeEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String work;
    private String status;
    private Date createAt;
    private Date editAt;
    private Date timeline;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
