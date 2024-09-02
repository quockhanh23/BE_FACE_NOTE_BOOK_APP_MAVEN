package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.LifeEvents;

import java.util.List;

public interface LifeEventsService extends GeneralService<LifeEvents> {

    void delete(LifeEvents entity);

    List<LifeEvents> findLifeEventsByUserId(Long idUser);
}
