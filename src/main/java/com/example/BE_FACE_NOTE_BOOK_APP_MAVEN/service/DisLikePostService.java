package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.DisLikePost;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DisLikePostService extends GeneralService<DisLikePost> {

    List<DisLikePost> findDisLike(@Param("idPost") Long idPost, @Param("idUser") Long idUser);

    List<DisLikePost> findAllDisLikeByPostId(@Param("idPost") Long idPost);

    List<DisLikePost> findAllByPostIdIn(List<Long> post_id);
}
