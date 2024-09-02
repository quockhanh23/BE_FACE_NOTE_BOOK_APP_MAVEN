package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.GroupPost;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupPostService extends GeneralService<GroupPost> {

    List<GroupPost> findAllPostByIdGroup(@Param("idGroup") Long idGroup);

    List<GroupPost> findAllPostWaiting(@Param("idGroup") Long idGroup);

    void deleteGroupPost(GroupPost groupPost);
}
