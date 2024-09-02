package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.AnswerComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerCommentRepository extends JpaRepository<AnswerComment, Long> {

    void delete(AnswerComment entity);

    List<AnswerComment> findAllByCommentIdAndDeleteAtIsNull(Long commentId);

    List<AnswerComment> findAllByDeleteAtIsNull();

    List<AnswerComment> findAllByCommentIdInAndDeleteAtIsNull(List<Long> comment_id);
}
