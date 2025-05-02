package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.PostDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Comment;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Post2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostService extends GeneralService<Post2> {

    Iterable<Post2> findAll();

    Page<Post2> findAllPostByUser(@Param("id") Long id, Pageable pageable);

    Page<Post2> allPost(Long id, Pageable pageable);

    void delete(Post2 entity);

    PostDTO mapper(Post2 post2);

    void createDefaultPost(Post2 post2);

    void saveAll(List<Post2> post2List);

    List<PostDTO> changeDTO(List<Post2> post2List);

    List<Post2> findAllByUserIdAndDeleteTrue(Long user_id);

    List<PostDTO> filterListPost(List<Post2> post2List);

    void deleteRelateOfComment(List<Comment> comments);

    Post2 checkExistPost(Long idPost);

    Page<PostDTO> allPostPublic(Long idUser, String type, int page, int size);
}
