package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.GroupParticipant;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.TheGroup;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupParticipantService extends GeneralService<GroupParticipant> {

    Iterable<GroupParticipant> findAll();

    List<GroupParticipant> findAllUserStatusPendingApproval(Long idTheGroup);

    List<GroupParticipant> findAllUserStatusApproved(Long idGroup);

    Optional<GroupParticipant> findByUserIdAndTheGroupId(Long user_id, Long theGroup_id);

    List<GroupParticipant> groupJoined(@Param("idUser") Long idUser);

    GroupParticipant createDefault(TheGroup theGroup, User user, String role);
}
