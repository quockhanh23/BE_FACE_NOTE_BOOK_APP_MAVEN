package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.EmailContent;

public interface EmailService {
    void sendMail(String email, String title, String content);

    void sendEmailWelCome(EmailContent emailContent);
}
