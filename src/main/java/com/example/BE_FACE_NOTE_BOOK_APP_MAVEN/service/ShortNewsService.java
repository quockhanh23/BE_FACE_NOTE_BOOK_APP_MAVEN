package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.ShortNews;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShortNewsService extends GeneralService<ShortNews> {

    Iterable<ShortNews> findAll();

    void delete(ShortNews entity);

    void createDefaultShortNews(ShortNews shortNews);

    void saveAll(List<ShortNews> shortNews);

    boolean checkYear(int year);

    List<ShortNews> findAllShortNews();

    List<ShortNews> findAllShortNewsPublic();

    List<ShortNews> myShortNew(@Param("idUser") Long idUser);

    List<ShortNews> getListShortNewInTrash(@Param("idUser") Long idUser);
}
