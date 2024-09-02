package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.TheGroup;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TheGroupService extends GeneralService<TheGroup> {

    List<TheGroup> findByIdUserCreate(Long idUserCreate);

    List<TheGroup> findAllGroup(@Param("idUserCreate") Long idUserCreate);

    List<TheGroup> findAllByIdIn(List<Long> id);

    List<TheGroup> searchAllByGroupNameAndType(@Param("searchText") String searchText, @Param("idUser") Long idUser);
}
