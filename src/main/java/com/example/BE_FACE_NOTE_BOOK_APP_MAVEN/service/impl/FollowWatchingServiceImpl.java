package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.FollowWatching;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.FollowWatchingRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.UserRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.FollowWatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FollowWatchingServiceImpl implements FollowWatchingService {

    private final FollowWatchingRepository followWatchingRepository;

    private final UserRepository userRepository;

    @Autowired
    public FollowWatchingServiceImpl(FollowWatchingRepository followWatchingRepository,
                                     UserRepository userRepository) {
        this.followWatchingRepository = followWatchingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createFollow(Long idUserLogin, Long idUserWatching) {
        if (idUserLogin != null && idUserWatching != null) {
            List<FollowWatching> watchingList = followWatchingRepository.findOne(idUserLogin, idUserWatching);
            if (CollectionUtils.isEmpty(watchingList)) {
                FollowWatching watching = new FollowWatching();
                watching.setCreateAt(new Date());
                watching.setIdUser(idUserLogin);
                watching.setStatus(Constants.FollowPeople.FOLLOW);
                watching.setIdUserTarget(idUserWatching);
                followWatchingRepository.save(watching);
            } else {
                watchingList.get(0).setStatus(Constants.FollowPeople.FOLLOW);
                followWatchingRepository.save(watchingList.get(0));
            }
        }
    }

    @Override
    public void unFollow(Long idUserLogin, Long idUserWatching) {
        if (idUserLogin != null && idUserWatching != null) {
            List<FollowWatching> watchingList = followWatchingRepository.findOne(idUserLogin, idUserWatching);
            if (!CollectionUtils.isEmpty(watchingList)) {
                watchingList.get(0).setStatus(Constants.FollowPeople.UNFOLLOW);
                followWatchingRepository.save(watchingList.get(0));
            }
        }
    }

    @Override
    public Object checkUserHadFollow(Long idUserLogin, Long idUserFollow) {
        Optional<User> userOptionalLogin = findById(idUserLogin);
        Optional<User> userOptionalFollow = findById(idUserFollow);
        if (userOptionalLogin.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUserLogin), HttpStatus.NOT_FOUND);
        }
        if (userOptionalFollow.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUserFollow), HttpStatus.NOT_FOUND);
        }
        List<FollowWatching> followWatchingList = followWatchingRepository.findOne(idUserLogin, idUserFollow);
        if (!CollectionUtils.isEmpty(followWatchingList)) {
            if (userOptionalLogin.get().getId().equals(followWatchingList.get(0).getIdUser())
                    && userOptionalFollow.get().getId().equals(followWatchingList.get(0).getIdUserTarget())) {
                return userOptionalFollow;
            }
        }
        return null;
    }

    private Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
