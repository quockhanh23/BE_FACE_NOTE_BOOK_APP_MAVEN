package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {

    String extractUsername(String token);
    String generateToken(UserDetails userDetails);
}
