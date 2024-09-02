package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.FollowWatching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowWatchingRepository extends JpaRepository<FollowWatching, Long> {

    @Modifying
    @Query(value = "select * from follow_watching where id_user = :idUserLogin and status = 'Follow'", nativeQuery = true)
    List<FollowWatching> getListFollowByIdUser(@Param("idUserLogin") Long idUserLogin);

    @Modifying
    @Query(value = "select * from follow_watching where id_user_target = :idUserTarget and status = 'Follow'", nativeQuery = true)
    List<FollowWatching> getListWatchingByIdUser(@Param("idUserTarget") Long idUserTarget);

    @Modifying
    @Query(value = "select * from follow_watching where id_user = :idUserLogin and id_user_target = :idUserTarget and status = 'Follow'", nativeQuery = true)
    List<FollowWatching> findOne(@Param("idUserLogin") Long idUserLogin, @Param("idUserTarget") Long idUserTarget);
}
