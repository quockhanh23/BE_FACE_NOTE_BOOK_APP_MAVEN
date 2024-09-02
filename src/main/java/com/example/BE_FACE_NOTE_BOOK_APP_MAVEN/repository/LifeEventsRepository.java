package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.LifeEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LifeEventsRepository extends JpaRepository<LifeEvents, Long> {

    List<LifeEvents> findLifeEventsByUserIdOrderByTimelineAsc(Long idUser);
}
