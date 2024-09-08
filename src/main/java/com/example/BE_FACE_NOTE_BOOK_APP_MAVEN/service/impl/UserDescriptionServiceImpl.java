package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.UserDescription;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.UserDescriptionRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.UserDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDescriptionServiceImpl implements UserDescriptionService {

    private final UserDescriptionRepository userDescriptionRepository;

    @Autowired
    public UserDescriptionServiceImpl(UserDescriptionRepository userDescriptionRepository) {
        this.userDescriptionRepository = userDescriptionRepository;
    }

    @Override
    @Cacheable(cacheNames = "findAllByUserId", key = "#user_id")
    public List<UserDescription> findAllByUserId(Long user_id) {
        return userDescriptionRepository.findAllByUserIdOrderByCreateAtDesc(user_id);
    }

    @Override
    @CacheEvict(cacheNames = {"findAllByUserId"}, allEntries = true)
    public void save(UserDescription userDescription) {
        userDescriptionRepository.save(userDescription);
    }

    @Override
    @CacheEvict(cacheNames = {"findAllByUserId"}, allEntries = true)
    public void delete(UserDescription userDescription) {
        userDescriptionRepository.delete(userDescription);
    }

    @Override
    public Optional<UserDescription> findById(Long id) {
        return userDescriptionRepository.findById(id);
    }
}
