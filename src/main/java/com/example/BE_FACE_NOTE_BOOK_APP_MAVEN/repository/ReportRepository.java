package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.ReportViolations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportViolations, Long> {

    List<ReportViolations> findAllByIdUserReportAndIdViolate(Long idUserReport, Long idViolate);
    List<ReportViolations> findAllByIdViolateAndType(Long idViolate, String type);
}
