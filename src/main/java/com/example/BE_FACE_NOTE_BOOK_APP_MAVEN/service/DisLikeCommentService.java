package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.DisLikeComment;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DisLikeCommentService {

    Optional<DisLikeComment> findById(Long id);

    List<DisLikeComment> findDisLikeComment(@Param("idComment") Long idComment, @Param("idUser") Long idUser);

    List<DisLikeComment> findAllByCommentIdAndUserDisLikeIsNotNull(@Param("idComment") Long idComment);

    void delete(DisLikeComment entity);

    DisLikeComment save(DisLikeComment disLikeComment);

    List<DisLikeComment> findAllByCommentIdIn(@Param("inputList") List<Long> inputList);
}
