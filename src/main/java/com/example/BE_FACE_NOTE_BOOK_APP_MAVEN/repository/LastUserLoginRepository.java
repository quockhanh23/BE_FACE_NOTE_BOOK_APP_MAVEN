package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.LastUserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LastUserLoginRepository extends JpaRepository<LastUserLogin, Long> {

    @Modifying
    @Query(value = "select * from last_user_login order by login_time desc limit 3", nativeQuery = true)
    List<LastUserLogin> historyLogin();

    LastUserLogin findByIdUser(Long id);
}
