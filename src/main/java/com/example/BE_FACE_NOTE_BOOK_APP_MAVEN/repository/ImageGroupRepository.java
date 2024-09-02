package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.ImageGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageGroupRepository extends JpaRepository<ImageGroup, Long> {
}
