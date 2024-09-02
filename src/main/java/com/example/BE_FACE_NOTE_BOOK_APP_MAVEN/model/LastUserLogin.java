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
public class LastUserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUser;
    private String userName;
    private String fullName;
    @Column(length = 1000)
    private String avatar;
    private Date loginTime;
    private String ipAddress;
}
