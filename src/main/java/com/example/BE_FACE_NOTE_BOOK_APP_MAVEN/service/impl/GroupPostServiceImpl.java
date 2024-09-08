package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.GroupPost;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.GroupPostRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.GroupPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupPostServiceImpl implements GroupPostService {

    private final GroupPostRepository groupPostRepository;

    @Autowired
    public GroupPostServiceImpl(GroupPostRepository groupPostRepository) {
        this.groupPostRepository = groupPostRepository;
    }

    @Override
    public Optional<GroupPost> findById(Long id) {
        return groupPostRepository.findById(id);
    }

    @Override
    public GroupPost save(GroupPost groupPost) {
        return groupPostRepository.save(groupPost);
    }

    @Override
    public List<GroupPost> findAllPostByIdGroup(Long idGroup) {
        return groupPostRepository.findAllPostByIdGroup(idGroup);
    }

    @Override
    public List<GroupPost> findAllPostWaiting(Long idGroup) {
        return groupPostRepository.findAllPostWaiting(idGroup);
    }

    @Override
    public void deleteGroupPost(GroupPost groupPost) {
        groupPostRepository.delete(groupPost);
    }
}
