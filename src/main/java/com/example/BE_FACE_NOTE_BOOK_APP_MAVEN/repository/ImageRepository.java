package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    void delete(Image entity);

    @Modifying
    @Query(value = "select * from image where (status = 'Public' or status = 'Private') and (delete_at is null) and id_user = :idUser", nativeQuery = true)
    List<Image> findAllImageByIdUser(@Param("idUser") Long idUser);

    @Modifying
    @Query(value = "select * from image where delete_at is not null and status = 'Delete' and id_user = :idUser", nativeQuery = true)
    List<Image> findAllImageDeletedByUserId(@Param("idUser") Long idUser);
}
