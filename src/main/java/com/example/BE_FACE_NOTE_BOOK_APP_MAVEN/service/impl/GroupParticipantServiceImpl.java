package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.GroupParticipant;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.TheGroup;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.GroupParticipantRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.GroupParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GroupParticipantServiceImpl implements GroupParticipantService {

    private final GroupParticipantRepository groupParticipantRepository;

    @Autowired
    public GroupParticipantServiceImpl(GroupParticipantRepository groupParticipantRepository) {
        this.groupParticipantRepository = groupParticipantRepository;
    }

    @Override
    public Iterable<GroupParticipant> findAll() {
        return groupParticipantRepository.findAll();
    }

    @Override
    public Optional<GroupParticipant> findById(Long id) {
        return groupParticipantRepository.findById(id);
    }

    @Override
    public GroupParticipant save(GroupParticipant groupParticipant) {
        return groupParticipantRepository.save(groupParticipant);
    }

    @Override
    public List<GroupParticipant> findAllUserStatusPendingApproval(Long idTheGroup) {
        return groupParticipantRepository.findAllUserStatusPendingApproval(idTheGroup);
    }

    @Override
    public List<GroupParticipant> findAllUserStatusApproved(Long idGroup) {
        return groupParticipantRepository.findAllUserStatusApproved(idGroup);
    }

    @Override
    public Optional<GroupParticipant> findByUserIdAndTheGroupId(Long user_id, Long theGroup_id) {
        return groupParticipantRepository.findByUserIdAndTheGroupId(user_id, theGroup_id);
    }

    @Override
    public List<GroupParticipant> groupJoined(Long idUser) {
        return groupParticipantRepository.groupJoined(idUser);
    }

    @Override
    public GroupParticipant createDefault(TheGroup theGroup, User user, String role) {
        GroupParticipant groupParticipant = new GroupParticipant();
        groupParticipant.setTheGroup(theGroup);
        groupParticipant.setUser(user);
        groupParticipant.setCreateAt(new Date());
        if (Constants.GroupStatus.MANAGEMENT.equals(role)) {
            groupParticipant.setStatus(Constants.GroupStatus.MANAGEMENT);
        }
        if (Constants.GroupStatus.STATUS_GROUP_PENDING.equals(role)) {
            groupParticipant.setStatus(Constants.GroupStatus.STATUS_GROUP_PENDING);
        }
        return groupParticipant;
    }
}
