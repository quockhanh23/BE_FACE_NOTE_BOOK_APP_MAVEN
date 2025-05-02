package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Post2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post2, Long> {

    @Query(value = "select * from post2 where user_id = :id and is_delete = false order by create_at desc", nativeQuery = true)
    Page<Post2> findAllPostByUser(@Param("id") Long id, Pageable pageable);

    @Query(value = "select *\n" +
            "from post2\n" +
            "where (status = 'Public' and is_delete = false and user_id != :id)\n" +
            "   or (is_delete = false and user_id = :id)\n" +
            "order by create_at desc", nativeQuery = true)
    Page<Post2> AllPost(@Param("id") Long id, Pageable pageable);

    @Query(value = "select * from post2 where is_delete is true and user_id = :id order by id desc", nativeQuery = true)
    List<Post2> findAllByUserIdAndDeleteIsTrue(@Param("id") Long id);
}
