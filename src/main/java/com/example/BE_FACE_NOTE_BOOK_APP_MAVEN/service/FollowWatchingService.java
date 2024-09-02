package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

public interface FollowWatchingService {

    void createFollow(Long idUserLogin, Long idUserFollow);

    void unFollow(Long idUserLogin, Long idUserFollow);

    Object checkUserHadFollow(Long idUserLogin, Long idUserFollow);
}
