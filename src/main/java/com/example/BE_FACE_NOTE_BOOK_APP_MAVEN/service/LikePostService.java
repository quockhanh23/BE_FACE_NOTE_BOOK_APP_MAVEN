package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.LikePost;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikePostService extends GeneralService<LikePost> {

    List<LikePost> findLike(@Param("idPost") Long idPost, @Param("idUser") Long idUser);

    List<LikePost> findAllLikeByPostId(@Param("idPost") Long idPost);

    List<LikePost> findAllByPostIdIn(List<Long> post_id);

    void delete(LikePost entity);
}
