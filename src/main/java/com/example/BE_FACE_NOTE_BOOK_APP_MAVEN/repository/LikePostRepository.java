package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {

    @Modifying
    @Query(value = "select * from like_post where post_id = :idPost and user_id = :idUser", nativeQuery = true)
    List<LikePost> findLike(@Param("idPost") Long idPost, @Param("idUser") Long idUser);

    List<LikePost> findAllByPostIdAndUserLikeIsNotNull(@Param("idPost") Long idPost);

    List<LikePost> findAllByPostIdInAndUserLikeIsNotNull(List<Long> post_id);

    void delete(LikePost entity);

    long countAllByPostIdAndUserLikeIsNotNull(@Param("idPost") Long idPost);
}
