package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.DisLikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisLikePostRepository extends JpaRepository<DisLikePost, Long> {

    @Modifying
    @Query(value = "select * from dis_like where post_id = :idPost and user_id = :idUser", nativeQuery = true)
    List<DisLikePost> findDisLike(@Param("idPost") Long idPost, @Param("idUser") Long idUser);

    List<DisLikePost> findAllByPostIdAndUserDisLikeIsNotNull(@Param("idPost") Long idPost);

    List<DisLikePost> findAllByPostIdInAndUserDisLikeIsNotNull(List<Long> post_id);

    void delete(DisLikePost entity);

    long countAllByPostIdAndUserDisLikeIsNotNull(@Param("idPost") Long idPost);
}
