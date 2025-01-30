package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.DisLikePost;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.DisLikePostRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.DisLikePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DislikePostServiceImpl implements DisLikePostService {

    private final DisLikePostRepository disLikeRepository;

    @Autowired
    public DislikePostServiceImpl(DisLikePostRepository disLikeRepository) {
        this.disLikeRepository = disLikeRepository;
    }

    @Override
    public Optional<DisLikePost> findById(Long id) {
        return disLikeRepository.findById(id);
    }

    @Override
    public DisLikePost save(DisLikePost disLikePost) {
        return disLikeRepository.save(disLikePost);
    }

    @Override
    public List<DisLikePost> findDisLike(Long idPost, Long idUser) {
        return disLikeRepository.findDisLike(idPost, idUser);
    }

    @Override
    public List<DisLikePost> findAllDisLikeByPostId(Long idPost) {
        return disLikeRepository.findAllByPostIdAndUserDisLikeIsNotNull(idPost);
    }

    @Override
    public List<DisLikePost> findAllByPostIdIn(List<Long> post_id) {
        return disLikeRepository.findAllByPostIdInAndUserDisLikeIsNotNull(post_id);
    }

    @Override
    public long countAllDisLikePostByPostId(Long idPost) {
        return disLikeRepository.countAllByPostIdAndUserDisLikeIsNotNull(idPost);
    }
}
