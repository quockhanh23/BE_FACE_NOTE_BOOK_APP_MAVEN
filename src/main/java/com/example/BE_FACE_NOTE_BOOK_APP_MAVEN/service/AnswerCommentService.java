package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.AnswerComment;

import java.util.List;

public interface AnswerCommentService extends GeneralService<AnswerComment> {

    void delete(AnswerComment entity);

    void create(AnswerComment answerComment);

    void saveAll(List<AnswerComment> answerComments);

    List<AnswerComment> findAllByCommentIdAndDeleteAtIsNull(Long commentId);

    List<AnswerComment> findAllByDeleteAtIsNull();

    List<AnswerComment> findAllByCommentIdIn(List<Long> comment_id);

}
