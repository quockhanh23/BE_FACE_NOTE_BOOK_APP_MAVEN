package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.DisLikeComment;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.DisLikeCommentRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.DisLikeCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisLikeCommentImpl implements DisLikeCommentService {

    @Autowired
    private DisLikeCommentRepository disLikeCommentRepository;

    @Override
    public Optional<DisLikeComment> findById(Long id) {
        return disLikeCommentRepository.findById(id);
    }

    @Override
    public List<DisLikeComment> findDisLikeComment(Long idComment, Long idUser) {
        return disLikeCommentRepository.findDisLikeComment(idComment, idUser);
    }

    @Override
    public List<DisLikeComment> findAllByCommentIdAndUserDisLikeIsNotNull(Long idComment) {
        return disLikeCommentRepository.findAllByCommentIdAndUserDisLikeIsNotNull(idComment);
    }

    @Override
    public void delete(DisLikeComment entity) {
        disLikeCommentRepository.delete(entity);
    }

    @Override
    public DisLikeComment save(DisLikeComment disLikeComment) {
        return disLikeCommentRepository.save(disLikeComment);
    }

    @Override
    public List<DisLikeComment> findAllByCommentIdIn(List<Long> inputList) {
        return disLikeCommentRepository.findAllByCommentIdIn(inputList);
    }
}
