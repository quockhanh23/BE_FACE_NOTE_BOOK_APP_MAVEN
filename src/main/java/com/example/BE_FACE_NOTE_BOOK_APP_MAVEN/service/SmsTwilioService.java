package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import java.util.List;

public interface SmsTwilioService {

    void sendSMS();

    void createNewPhoneNumberInTwilio(String phoneNumber);

    void deletePhoneNumberInTwilio(String phoneNumber);

    List<String> listNumberPhoneTwilio();
}
