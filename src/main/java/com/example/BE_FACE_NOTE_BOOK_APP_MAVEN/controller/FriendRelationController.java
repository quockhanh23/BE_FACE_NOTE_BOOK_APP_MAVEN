package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Common;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.UserDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.UserNotificationDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.FollowWatching;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.FriendRelation;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Notification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.FollowWatchingRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.FriendRelationService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.NotificationService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/friends")
@Slf4j
@RequiredArgsConstructor
public class FriendRelationController {

    private final FriendRelationService friendRelationService;

    private final UserService userService;

    private final FollowWatchingRepository followWatchingRepository;

    private final NotificationService notificationService;

    // Danh sách người lạ ở phần gợi ý chung
    @GetMapping("/allPeople")
    public ResponseEntity<Object> listPeople(@RequestParam Long idUser) {
        List<User> listPeople = userService.listPeople(idUser);
        if (!CollectionUtils.isEmpty(listPeople)) {
            listPeople.removeIf(item -> item.getId().equals(idUser));
        }
        List<UserNotificationDTO> list = friendRelationService.listUser(listPeople);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/friendCheck")
    public ResponseEntity<Object> friendCheck(@RequestParam Long idUser) {
        List<FriendRelation> friendRelations = friendRelationService.listRequest2(idUser);
        if (CollectionUtils.isEmpty(friendRelations)) {
            friendRelations = new ArrayList<>();
        }
        return new ResponseEntity<>(friendRelations, HttpStatus.OK);
    }

    @GetMapping("/friendWaiting")
    public ResponseEntity<Object> friendWaiting(@RequestParam Long idFriend, @RequestParam Long idLogin) {
        List<FriendRelation> friendRelations = friendRelationService.friendWaiting(idFriend, idLogin);
        if (CollectionUtils.isEmpty(friendRelations)) {
            friendRelations = new ArrayList<>();
        }
        return new ResponseEntity<>(friendRelations, HttpStatus.OK);
    }

    @GetMapping("/allFriend")
    public ResponseEntity<Object> allFriend(@RequestParam Long idFriend, @RequestParam Long idLogin) {
        List<FriendRelation> friendRelations = friendRelationService.allFriend(idFriend, idLogin);
        if (CollectionUtils.isEmpty(friendRelations)) {
            friendRelations = new ArrayList<>();
        }
        return new ResponseEntity<>(friendRelations, HttpStatus.OK);
    }

    // Danh sách những người đã gửi lời mời kết bạn
    @GetMapping("/findAllListRequestAddFriendById")
    public ResponseEntity<List<?>> findAllListRequestAddFriendById(@RequestParam Long idUser) {
        List<FriendRelation> friendRelations = friendRelationService.findAllListRequestAddFriendById(idUser);
        List<UserDTO> userDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(friendRelations)) {
            List<Long> idFriend = friendRelations.stream()
                    .map(item -> item.getUserLogin().getId()).distinct().collect(Collectors.toList());
            List<User> userList = userService.findAllByIdIn(idFriend);
            userList.removeIf(item -> item.getId().equals(idUser));
            userDTOList = friendRelationService.listResult(userList);
        }
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    // Danh sách bạn đã gửi lời mời kết bạn
    @GetMapping("/listRequest")
    public ResponseEntity<Object> listRequest(@RequestParam Long idUser) {
        final double startTime = System.currentTimeMillis();
        List<FriendRelation> friendRelationList = friendRelationService.listRequest(idUser);
        List<UserDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(friendRelationList)) {
            List<Long> idFriend = friendRelationList.stream()
                    .map(FriendRelation::getIdFriend).distinct().collect(Collectors.toList());
            List<User> userList = userService.findAllByIdIn(idFriend);
            list = friendRelationService.listResult(userList);
        }
        final double elapsedTimeMillis = System.currentTimeMillis();
        log.info(Constants.MESSAGE_STRIKE_THROUGH);
        Common.executionTime(startTime, elapsedTimeMillis);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // Danh sách bạn bè
    @GetMapping("/listFriend")
    public ResponseEntity<Object> listFriend(@RequestParam Long idUser) {
        List<UserDTO> userDTOList = userService.listFriend(idUser);
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    // Danh sách bạn bè giới hạn
    @GetMapping("/listFriendShowAvatarLimit")
    public ResponseEntity<Object> listFriendShowAvatarLimit(@RequestParam Long idUser) {
        List<User> listFriend = userService.allFriendByUserId(idUser);
        List<UserDTO> userDTOList = listFriend.stream()
                .map(x -> new UserDTO(x.getId(), x.getFullName(), x.getAvatar(), x.getCover()))
                .limit(5)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    // Số lượng bạn chung
    @GetMapping("/mutualFriends")
    public ResponseEntity<Object> mutualFriends(@RequestParam Long idUserLogin, @RequestParam Long idUser) {
        List<User> listFriend = userService.allFriendByUserId(idUserLogin);
        List<User> friendOfFriend = userService.allFriendByUserId(idUser);
        List<Long> mutualFriends = new ArrayList<>();
        if (!CollectionUtils.isEmpty(listFriend) && !CollectionUtils.isEmpty(friendOfFriend)) {
            for (User value : listFriend) {
                for (User user : friendOfFriend) {
                    if (value.getId().equals(user.getId())) {
                        mutualFriends.add(user.getId());
                    }
                }
            }
        }
        return new ResponseEntity<>(mutualFriends.size(), HttpStatus.OK);
    }

    // Gửi lời mời kết bạn
    @DeleteMapping("/sendRequestFriend")
    public ResponseEntity<Object> senRequestFriend(@RequestParam Long idUser, @RequestParam Long idFriend) {
        Optional<FriendRelation> optionalFriendRelation = friendRelationService.findByIdUserAndIdFriend(idUser, idFriend);
        Optional<FriendRelation> optionalFriendRelation2 = friendRelationService.findByIdUserAndIdFriend(idFriend, idUser);
        User user = userService.checkExistUser(idFriend);
        User user2 = userService.checkExistUser(idUser);
        if (Objects.equals(idUser, idFriend)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (user.getStatus().equals(Constants.STATUS_BANED)) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
        if (optionalFriendRelation.isEmpty()) {
            FriendRelation friendRelation = friendRelationService.createDefaultStatusWaiting();
            friendRelation.setUserLogin(user2);
            friendRelation.setIdFriend(idFriend);
            friendRelation.setIdUser(idUser);
            friendRelation.setFriend(user2);
            friendRelationService.save(friendRelation);
        } else {
            optionalFriendRelation.get().setStatusFriend(Constants.WAITING);
            optionalFriendRelation.get().setUserLogin(user2);
            friendRelationService.save(optionalFriendRelation.get());
        }
        if (optionalFriendRelation2.isEmpty()) {
            FriendRelation friendRelation = friendRelationService.createDefaultStatusWaiting();
            friendRelation.setFriend(user2);
            friendRelation.setIdUser(idFriend);
            friendRelation.setIdFriend(idUser);
            friendRelation.setUserLogin(user2);
            friendRelationService.save(friendRelation);
        } else {
            optionalFriendRelation2.get().setStatusFriend(Constants.WAITING);
            optionalFriendRelation2.get().setUserLogin(user2);
            friendRelationService.save(optionalFriendRelation2.get());
        }
        String title = Constants.Notification.TITLE_SEND_REQUEST_FRIEND;
        String type = Constants.Notification.TYPE_FRIEND;
        Notification notification = notificationService.createDefault(user, user2, title, idFriend, type);
        notificationService.save(notification);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Đồng ý kết bạn, Hủy kết bạn, Hủy yêu cầu kết bạn
    @DeleteMapping("/actionRequestFriend")
    public ResponseEntity<Object> actionRequestFriend(@RequestParam Long idUser,
                                                      @RequestParam Long idFriend,
                                                      @RequestParam String type) {
        Optional<FriendRelation> optionalFriendRelation = friendRelationService.findByIdUserAndIdFriend(idUser, idFriend);
        Optional<FriendRelation> optionalFriendRelation2 = friendRelationService.findByIdUserAndIdFriend(idFriend, idUser);
        if (optionalFriendRelation.isEmpty() || optionalFriendRelation2.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (Objects.equals(idUser, idFriend)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.checkExistUser(idFriend);
        User user2 = userService.checkExistUser(idUser);
        if ("accept".equalsIgnoreCase(type)) {
            if (user.getStatus().equals(Constants.STATUS_BANED)) {
                return new ResponseEntity<>(HttpStatus.LOCKED);
            }
            friendRelationService.saveAction(optionalFriendRelation.get(),
                    optionalFriendRelation2.get(), Constants.FRIEND);
            List<FollowWatching> followWatchingList = followWatchingRepository.findOne(idUser, idFriend);
            if (!CollectionUtils.isEmpty(followWatchingList)) {
                followWatchingList.get(0).setStatus(Constants.FRIEND);
            }
            String title = Constants.Notification.TITLE_AGREE_FRIEND;
            String typeNotification = Constants.Notification.TYPE_FRIEND;
            Notification notification = notificationService.createDefault(user,
                    user2, title, idFriend, typeNotification);
            notificationService.save(notification);
        }
        if ("delete".equalsIgnoreCase(type)) {
            friendRelationService.saveAction(optionalFriendRelation.get(),
                    optionalFriendRelation2.get(), Constants.NO_FRIEND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 12 gợi ý kết bạn
    @GetMapping("/friendSuggestion")
    public ResponseEntity<Object> friendSuggestion(@RequestParam Long idUser) {
        List<User> listSuggestion = userService.friendSuggestion(idUser);
        List<UserNotificationDTO> list = friendRelationService.listUser(listSuggestion);
        Collections.shuffle(list);
        int maxIndex;
        if (list.size() >= 13) {
            maxIndex = 12;
        } else {
            maxIndex = list.size();
        }
        List<UserNotificationDTO> newList = list.subList(0, maxIndex);
        return new ResponseEntity<>(newList, HttpStatus.OK);
    }
}
