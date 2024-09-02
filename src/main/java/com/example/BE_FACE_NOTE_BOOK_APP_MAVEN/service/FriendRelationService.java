package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.UserDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.UserNotificationDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.FriendRelation;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRelationService extends GeneralService<FriendRelation> {

    Optional<FriendRelation> findByIdUserAndIdFriend(Long idUser, Long idFriend);

    List<FriendRelation> findAllListRequestAddFriendById(Long idUser);

    List<FriendRelation> listRequest(@Param("idUser") Long idUser);

    List<FriendRelation> listRequest2(@Param("idUser") Long idUser);

    List<FriendRelation> friendWaiting(@Param("idFriend") Long idFriend, @Param("idLogin") Long idLogin);

    List<FriendRelation> allFriend(@Param("idFriend") Long idFriend, @Param("idLogin") Long idLogin);

    FriendRelation createDefaultStatusWaiting();

    List<UserNotificationDTO> listUser(List<User> userList);

    void saveAction(FriendRelation friendRelation1, FriendRelation friendRelation2, String status);

    List<UserDTO> listResult(List<User> userList);
}
