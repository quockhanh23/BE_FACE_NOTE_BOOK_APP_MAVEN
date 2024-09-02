package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model;

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
public class TheGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String groupName;
    private Date createAt;
    private String createBy;
    private String status;
    private String type;
    private String subtype;
    private Long numberUser;
    @Column(length = 1000)
    private String avatarGroup;
    @Column(length = 1000)
    private String coverGroup;
    private Long idUserCreate;
}
