package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String linkImage;
    private String status;
    private Date createAt = new Date();
    private Date deleteAt;
    private Long idTheGroup;
    private Long idUserUpLoad;
}
