package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.UserDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.UserNotificationDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.FriendRelation;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.FriendRelationRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.FriendRelationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendRelationServiceImpl implements FriendRelationService {

    private final FriendRelationRepository friendRelationRepository;

    @Autowired
    public FriendRelationServiceImpl(FriendRelationRepository friendRelationRepository) {
        this.friendRelationRepository = friendRelationRepository;
    }

    @Override
    public Optional<FriendRelation> findById(Long id) {
        return friendRelationRepository.findById(id);
    }

    @Override
    @CacheEvict(cacheNames = {"findAllListRequestAddFriendById", "listRequest", "listRequest2"}, allEntries = true)
    public FriendRelation save(FriendRelation friendRelation) {
        return friendRelationRepository.save(friendRelation);
    }

    @Override
    public Optional<FriendRelation> findByIdUserAndIdFriend(Long idUser, Long idFriend) {
        return friendRelationRepository.findByIdUserAndIdFriend(idUser, idFriend);
    }

    @Override
    @Cacheable(cacheNames = "findAllListRequestAddFriendById", key = "#idUser")
    public List<FriendRelation> findAllListRequestAddFriendById(Long idUser) {
        return friendRelationRepository.findAllListRequestAddFriendById(idUser);
    }

    @Override
    @Cacheable(cacheNames = "listRequest", key = "#idUser")
    public List<FriendRelation> listRequest(Long idUser) {
        return friendRelationRepository.listRequest(idUser);
    }

    @Override
    @Cacheable(cacheNames = "listRequest2", key = "#idUser")
    public List<FriendRelation> listRequest2(Long idUser) {
        return friendRelationRepository.listRequest2(idUser);
    }

    @Override
    public List<FriendRelation> friendWaiting(Long idFriend, Long idLogin) {
        return friendRelationRepository.friendWaiting(idFriend, idLogin);
    }

    @Override
    public List<FriendRelation> allFriend(Long idFriend, Long idLogin) {
        return friendRelationRepository.allFriend(idFriend, idLogin);
    }

    @Override
    public FriendRelation createDefaultStatusWaiting() {
        FriendRelation friendRelation = new FriendRelation();
        friendRelation.setStatusFriend(Constants.WAITING);
        return friendRelation;
    }

    @Override
    public List<UserNotificationDTO> listUser(List<User> userList) {
        List<UserNotificationDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userList)) {
            userList.forEach(user -> {
                UserNotificationDTO userDTO = new UserNotificationDTO();
                BeanUtils.copyProperties(user, userDTO);
                list.add(userDTO);
            });
        }
        return list;
    }

    @Override
    public void saveAction(FriendRelation friendRelation1, FriendRelation friendRelation2, String status) {
        friendRelation1.setStatusFriend(status);
        friendRelation2.setStatusFriend(status);
        save(friendRelation1);
        save(friendRelation2);
    }

    @Override
    public List<UserDTO> listResult(List<User> userList) {
        List<UserDTO> userDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(userList)) return userDTOList;
        return userList.stream().map(x -> new UserDTO(x.getId(), x.getFullName(),
                        x.getAvatar(), x.getCover())).collect(Collectors.toList());

    }
}
