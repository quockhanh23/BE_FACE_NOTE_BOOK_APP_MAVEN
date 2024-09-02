package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String linkImage;
    private String status;
    private Date createAt = new Date();
    private Date deleteAt;
    private Long idUser;
}
