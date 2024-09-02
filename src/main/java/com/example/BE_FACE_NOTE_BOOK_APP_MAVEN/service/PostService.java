package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.PostDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Comment;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Post2;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostService extends GeneralService<Post2> {

    Iterable<Post2> findAll();

    List<Post2> findAllPostByUser(@Param("id") Long id);

    List<Post2> allPost(Long id);

    void delete(Post2 entity);

    PostDTO mapper(Post2 post2);

    void create(Post2 post2);

    void saveAll(List<Post2> post2List);

    List<PostDTO> changeDTO(List<Post2> post2List);

    List<Post2> findAllByUserIdAndDeleteTrue(Long user_id);

    List<PostDTO> filterListPost(List<Post2> post2List);

    void deleteRelateOfComment(List<Comment> comments);
}
