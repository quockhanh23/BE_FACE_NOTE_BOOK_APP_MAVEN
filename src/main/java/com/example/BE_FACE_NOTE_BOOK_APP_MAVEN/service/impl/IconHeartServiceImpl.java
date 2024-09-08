package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.IconHeart;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.IconHeartRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.IconHeartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IconHeartServiceImpl implements IconHeartService {

    private final IconHeartRepository iconHeartRepository;

    @Autowired
    public IconHeartServiceImpl(IconHeartRepository iconHeartRepository) {
        this.iconHeartRepository = iconHeartRepository;
    }

    @Override
    public Optional<IconHeart> findById(Long id) {
        return iconHeartRepository.findById(id);
    }

    @Override
    public IconHeart save(IconHeart iconHeart) {
        return iconHeartRepository.save(iconHeart);
    }


    @Override
    public void delete(IconHeart entity) {
        iconHeartRepository.delete(entity);
    }

    @Override
    public List<IconHeart> findHeart(Long idPost, Long idUser) {
        return iconHeartRepository.findHeart(idPost, idUser);
    }

    @Override
    public List<IconHeart> findAllHeartByPostId(Long idPost) {
        return iconHeartRepository.findAllByPostIdAndUserIsNotNull(idPost);
    }

    @Override
    public List<IconHeart> findAllByPostIdIn(List<Long> post_id) {
        return iconHeartRepository.findAllByPostIdInAndUserIsNotNull(post_id);
    }
}
