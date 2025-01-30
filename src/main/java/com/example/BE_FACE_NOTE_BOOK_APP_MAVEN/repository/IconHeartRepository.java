package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.IconHeart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IconHeartRepository extends JpaRepository<IconHeart, Long> {

    @Modifying
    @Query(value = "select * from icon_heart where post_id = :idPost and user_id = :idUser", nativeQuery = true)
    List<IconHeart> findHeart(@Param("idPost") Long idPost, @Param("idUser") Long idUser);

    List<IconHeart> findAllByPostIdAndUserIsNotNull(@Param("idPost") Long idPost);

    List<IconHeart> findAllByPostIdInAndUserIsNotNull(List<Long> post_id);

    long countAllByPostIdAndUserIsNotNull(@Param("idPost") Long idPost);
}
