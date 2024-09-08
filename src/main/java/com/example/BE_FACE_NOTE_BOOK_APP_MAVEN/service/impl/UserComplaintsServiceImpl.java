package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.UserComplaints;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.UserComplaintsRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.UserComplaintsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserComplaintsServiceImpl implements UserComplaintsService {

    private final UserComplaintsRepository userComplaintsRepository;

    @Autowired
    public UserComplaintsServiceImpl(UserComplaintsRepository userComplaintsRepository) {
        this.userComplaintsRepository = userComplaintsRepository;
    }

    @Override
    public Optional<UserComplaints> findById(Long id) {
        return userComplaintsRepository.findById(id);
    }

    @Override
    public UserComplaints save(UserComplaints userComplaints) {
        return userComplaintsRepository.save(userComplaints);
    }
}
