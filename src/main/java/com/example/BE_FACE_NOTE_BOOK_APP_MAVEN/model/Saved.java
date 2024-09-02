package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Saved {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUser;
    private String status;
    private Date saveDate;
    private String type;
    private Long idPost;
    private String userCreate;
    private String groupName;
    private String imagePost;
    private String content;
}
