package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Saved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedRepository extends JpaRepository<Saved, Long> {

    @Modifying
    @Query(value = "select * from saved where status = 'Saved' and id_user = :idUser", nativeQuery = true)
    List<Saved> findAllSavedPost(@Param("idUser") Long idUser);
}
