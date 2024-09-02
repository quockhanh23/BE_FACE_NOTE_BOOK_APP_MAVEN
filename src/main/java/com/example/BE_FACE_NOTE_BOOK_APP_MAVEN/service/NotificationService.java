package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Notification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationService {

    void delete(Notification notification);

    void deleteAll(List<Notification> notifications);

    void save(Notification notification);

    void saveAll(List<Notification> notifications);

    Notification createDefault(User idSendTo, User idAction, String title, Long typeId, String type);

    List<Notification> findAllByIdSendTo(@Param("idSendTo") Long idSendTo);

    List<Notification> findAllByIdSendToNotSeen(@Param("idSendTo") Long idSendTo);
}
