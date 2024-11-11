package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.CommentDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Comment;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentService extends GeneralService<Comment> {

    List<Comment> findAllByUserId(@Param("id") Long id);

    List<Comment> getCommentByIdPost(@Param("id") Long id);

    List<Comment> getCommentTrue();

    CommentDTO mapper(Comment comment);

    void createDefault(Comment comment);

    void saveAll(List<Comment> comments);

    List<Comment> findAllByIdIn(@Param("inputList") List<Long> inputList);

    List<Comment> findAllByPostIdIn(List<Long> post_id);
}
