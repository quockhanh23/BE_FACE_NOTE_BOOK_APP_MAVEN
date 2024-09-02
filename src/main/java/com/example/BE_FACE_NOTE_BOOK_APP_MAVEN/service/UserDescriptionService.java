package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.UserDescription;

import java.util.List;
import java.util.Optional;

public interface UserDescriptionService {

    List<UserDescription> findAllByUserId(Long user_id);

    void save(UserDescription userDescription);

    void delete(UserDescription userDescription);

    Optional<UserDescription> findById(Long id);
}
