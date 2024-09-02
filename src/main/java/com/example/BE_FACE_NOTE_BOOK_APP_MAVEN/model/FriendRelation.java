package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "friendRelation")
public class FriendRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUser;
    private Long idFriend;
    private String statusFriend;
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;
    @ManyToOne
    @JoinColumn(name = "userLogin_id")
    private User userLogin;
}
