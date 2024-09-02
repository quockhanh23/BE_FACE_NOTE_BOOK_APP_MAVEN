package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.LikeComment;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeCommentService extends GeneralService<LikeComment> {

    List<LikeComment> findLikeComment(@Param("idComment") Long idComment, @Param("idUser") Long idUser);

    List<LikeComment> findAllByCommentIdAndUserLikeIsNotNull(@Param("idComment") Long idComment);

    void delete(LikeComment entity);

    List<LikeComment> findAllByCommentIdIn(@Param("inputList") List<Long> inputList);
}
