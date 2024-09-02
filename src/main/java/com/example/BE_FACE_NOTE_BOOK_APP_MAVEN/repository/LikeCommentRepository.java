package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {

    @Modifying
    @Query(value = "select * from like_comment where comment_id = :idComment and user_id = :idUser", nativeQuery = true)
    List<LikeComment> findLikeComment(@Param("idComment") Long idComment, @Param("idUser") Long idUser);

    List<LikeComment> findAllByCommentIdAndUserLikeIsNotNull(@Param("idComment") Long idComment);

    List<LikeComment> findAllByCommentIdIn(@Param("inputList") List<Long> inputList);
}
