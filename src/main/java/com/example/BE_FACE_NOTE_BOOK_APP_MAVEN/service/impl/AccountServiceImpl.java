package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.UserRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//@Service(value = "accountServiceImpl")
//@Scope(value = "singleton")
public class AccountServiceImpl implements GeneralService<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public User save(User user) {
        return userRepository.save(user);
    }

}
