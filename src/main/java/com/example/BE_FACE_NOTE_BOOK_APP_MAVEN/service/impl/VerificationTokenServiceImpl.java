package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.VerificationToken;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.VerificationTokenRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerificationTokenServiceImpl(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public void save(VerificationToken token) {
        verificationTokenRepository.save(token);
    }

    @Override
    public List<VerificationToken> findAll() {
        return verificationTokenRepository.findAll();
    }
}
