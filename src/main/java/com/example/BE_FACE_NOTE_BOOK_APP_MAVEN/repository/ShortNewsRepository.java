package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.ShortNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortNewsRepository extends JpaRepository<ShortNews, Long> {

    void delete(ShortNews entity);

    @Modifying
    @Query(value = "select * from short_news where remaining >= 0 order by create_at desc limit 4", nativeQuery = true)
    List<ShortNews> findAllShortNews();

    @Modifying
    @Query(value = "select * from short_news where status = 'Public' order by create_at desc", nativeQuery = true)
    List<ShortNews> findAllShortNewsPublic();

    @Modifying
    @Query(value = "select * from short_news where user_id= :idUser and is_delete = false order by create_at desc", nativeQuery = true)
    List<ShortNews> myShortNew(@Param("idUser") Long idUser);

    @Modifying
    @Query(value = "select * from short_news where is_delete is true and user_id = :idUser order by id desc", nativeQuery = true)
    List<ShortNews> getListShortNewInTrash(@Param("idUser") Long idUser);
}
