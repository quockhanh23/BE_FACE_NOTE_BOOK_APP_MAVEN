package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.LikeComment;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.LikeCommentRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.LikeCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeCommentServiceImpl implements LikeCommentService {

    private final LikeCommentRepository likeCommentRepository;

    @Autowired
    public LikeCommentServiceImpl(LikeCommentRepository likeCommentRepository) {
        this.likeCommentRepository = likeCommentRepository;
    }

    @Override
    public Optional<LikeComment> findById(Long id) {
        return likeCommentRepository.findById(id);
    }

    @Override
    public LikeComment save(LikeComment likeComment) {
        return likeCommentRepository.save(likeComment);
    }

    @Override
    public List<LikeComment> findLikeComment(Long idPost, Long idUser) {
        return likeCommentRepository.findLikeComment(idPost, idUser);
    }

    @Override
    public List<LikeComment> findAllByCommentIdAndUserLikeIsNotNull(Long idComment) {
        return likeCommentRepository.findAllByCommentIdAndUserLikeIsNotNull(idComment);
    }

    @Override
    public void delete(LikeComment entity) {
        likeCommentRepository.delete(entity);
    }

    @Override
    public List<LikeComment> findAllByCommentIdIn(List<Long> inputList) {
        return likeCommentRepository.findAllByCommentIdIn(inputList);
    }
}
