package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.TheGroup;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.TheGroupRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.TheGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TheGroupServiceImpl implements TheGroupService {

    @Autowired
    private TheGroupRepository theGroupRepository;

    @Override
    public Optional<TheGroup> findById(Long id) {
        return theGroupRepository.findById(id);
    }

    @Override
    public TheGroup save(TheGroup theGroup) {
        return theGroupRepository.save(theGroup);
    }

    @Override
    public List<TheGroup> findByIdUserCreate(Long idUserCreate) {
        return theGroupRepository.findByIdUserCreate(idUserCreate);
    }

    @Override
    public List<TheGroup> findAllGroup(Long idUserCreate) {
        return theGroupRepository.findAllGroup(idUserCreate);
    }

    @Override
    public List<TheGroup> findAllByIdIn(List<Long> id) {
        return theGroupRepository.findAllByIdIn(id);
    }

    @Override
    public List<TheGroup> searchAllByGroupNameAndType(String searchText, Long idUser) {
        return theGroupRepository.searchAllByGroupNameAndType(searchText, idUser);
    }
}
