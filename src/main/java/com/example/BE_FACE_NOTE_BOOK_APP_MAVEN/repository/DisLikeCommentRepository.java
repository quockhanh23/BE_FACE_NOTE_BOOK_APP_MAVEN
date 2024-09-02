package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.DisLikeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisLikeCommentRepository extends JpaRepository<DisLikeComment, Long> {

    @Modifying
    @Query(value = "select * from dis_like_comment where comment_id = :idComment and user_id = :idUser", nativeQuery = true)
    List<DisLikeComment> findDisLikeComment(@Param("idComment") Long idComment, @Param("idUser") Long idUser);

    List<DisLikeComment> findAllByCommentIdAndUserDisLikeIsNotNull(@Param("idComment") Long idComment);

    List<DisLikeComment> findAllByCommentIdIn(@Param("inputList") List<Long> inputList);
}
