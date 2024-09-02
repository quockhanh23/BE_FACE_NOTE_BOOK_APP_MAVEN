package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByUserId(@Param("id") Long id);

    @Modifying
    @Query(value = "select * from comment where post_id = :id and is_delete = false", nativeQuery = true)
    List<Comment> getCommentByIdPost(@Param("id") Long id);

    @Modifying
    @Query(value = "select * from comment where is_delete = false", nativeQuery = true)
    List<Comment> getCommentTrue();

    List<Comment> findAllByIdIn(@Param("inputList") List<Long> inputList);

    List<Comment> findAllByPostIdInAndDeleteAtIsNull(List<Long> post_id);
}
