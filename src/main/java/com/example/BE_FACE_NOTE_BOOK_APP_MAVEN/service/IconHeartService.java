package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.IconHeart;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IconHeartService extends GeneralService<IconHeart> {

    void delete(IconHeart entity);

    List<IconHeart> findHeart(@Param("idPost") Long idPost, @Param("idUser") Long idUser);

    List<IconHeart> findAllHeartByPostId(@Param("idPost") Long idPost);

    List<IconHeart> findAllByPostIdIn(List<Long> post_id);

    long countAllIconHeartByPostId(@Param("idPost") Long idPost);
}
