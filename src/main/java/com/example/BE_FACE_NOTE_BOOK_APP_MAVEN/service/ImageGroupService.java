package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.ImageGroup;

public interface ImageGroupService {

    ImageGroup createImageGroupDefault(String image, Long idTheGroup, Long idUserUpLoad);

    void save(ImageGroup imageGroup);
}
