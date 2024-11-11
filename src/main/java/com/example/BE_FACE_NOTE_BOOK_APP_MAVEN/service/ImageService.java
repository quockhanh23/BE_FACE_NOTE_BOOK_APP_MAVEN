package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Image;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageService extends GeneralService<Image> {

    void delete(Image entity);

    List<Image> findAllImageByIdUser(@Param("idUser") Long idUser);

    Image createImageDefault(String imageUrl, User user);

    List<Image> findAllImageDeletedByUserId(@Param("idUser") Long idUser);
}
