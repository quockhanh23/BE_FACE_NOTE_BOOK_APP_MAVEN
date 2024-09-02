package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {

    void init();

    void save(MultipartFile file);

    void deleteAll();
}
