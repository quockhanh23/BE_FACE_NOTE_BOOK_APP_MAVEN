package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import java.util.Optional;

public interface GeneralService<T> {

    Optional<T> findById(Long id);

    T save(T t);
}
