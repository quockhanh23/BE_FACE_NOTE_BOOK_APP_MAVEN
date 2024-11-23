package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.ShortNews;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShortNewsService extends GeneralService<ShortNews> {

    void delete(ShortNews entity);

    void createShortNews(ShortNews shortNews, User user);

    void saveAll(List<ShortNews> shortNews);

    List<ShortNews> findAllShortNews();

    List<ShortNews> findAllShortNewsPublic();

    List<ShortNews> myShortNew(@Param("idUser") Long idUser);

    List<ShortNews> getListShortNewInTrash(@Param("idUser") Long idUser);

    void checkExpiryDate();
}
