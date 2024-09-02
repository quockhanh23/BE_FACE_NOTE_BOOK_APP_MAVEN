package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.UserDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDescriptionRepository extends JpaRepository<UserDescription, Long> {

    List<UserDescription> findAllByUserIdOrderByCreateAtDesc(Long user_id);
}
