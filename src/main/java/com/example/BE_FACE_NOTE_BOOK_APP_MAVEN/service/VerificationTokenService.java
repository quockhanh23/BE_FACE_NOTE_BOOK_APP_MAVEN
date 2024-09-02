package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;


import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.VerificationToken;

import java.util.List;

public interface VerificationTokenService {

    void save(VerificationToken token);

    List<VerificationToken> findAll();
}
