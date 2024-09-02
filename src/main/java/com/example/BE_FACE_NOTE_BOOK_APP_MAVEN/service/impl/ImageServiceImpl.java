package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Image;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.ImageRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Optional<Image> findById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    @CacheEvict(cacheNames = {"findAllImageByIdUser", "findAllImageDeletedByUserId"}, allEntries = true)
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Override
    @CacheEvict(cacheNames = {"findAllImageByIdUser", "findAllImageDeletedByUserId"}, allEntries = true)
    public void delete(Image entity) {
        imageRepository.delete(entity);
    }

    @Override
    @Cacheable(cacheNames = "findAllImageByIdUser", key = "#idUser")
    public List<Image> findAllImageByIdUser(Long idUser) {
        return imageRepository.findAllImageByIdUser(idUser);
    }

    @Override
    public Image createImageDefault(String image, User user) {
        Image image1 = new Image();
        image1.setLinkImage(image);
        image1.setStatus(Constants.STATUS_PUBLIC);
        image1.setDeleteAt(null);
        image1.setIdUser(user.getId());
        return image1;
    }

    @Override
    @Cacheable(cacheNames = "findAllImageDeletedByUserId", key = "#idUser")
    public List<Image> findAllImageDeletedByUserId(Long idUser) {
        return imageRepository.findAllImageDeletedByUserId(idUser);
    }
}
