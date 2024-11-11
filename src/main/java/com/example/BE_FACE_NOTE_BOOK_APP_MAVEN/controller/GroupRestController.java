package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Common;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.MessageResponse;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.*;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.GroupParticipantRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/groups")
@Slf4j
public class GroupRestController {

    private final GroupParticipantService groupParticipantService;

    private final TheGroupService theGroupService;

    private final GroupPostService groupPostService;

    private final UserService userService;

    private final ImageGroupService imageGroupService;

    private final NotificationService notificationService;

    private final GroupParticipantRepository groupParticipantRepository;

    @Autowired
    public GroupRestController(GroupParticipantService groupParticipantService,
                               TheGroupService theGroupService,
                               GroupPostService groupPostService,
                               UserService userService,
                               ImageGroupService imageGroupService,
                               NotificationService notificationService,
                               GroupParticipantRepository groupParticipantRepository) {
        this.groupParticipantService = groupParticipantService;
        this.theGroupService = theGroupService;
        this.groupPostService = groupPostService;
        this.userService = userService;
        this.imageGroupService = imageGroupService;
        this.notificationService = notificationService;
        this.groupParticipantRepository = groupParticipantRepository;
    }

    // Danh sách nhóm đã vào
    @GetMapping("/groupJoined")
    public ResponseEntity<Object> groupJoined(@RequestParam Long idUser) {
        List<GroupParticipant> groupParticipants = groupParticipantService.groupJoined(idUser);
        List<TheGroup> theGroupList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(groupParticipants)) {
            List<Long> listId = groupParticipants.stream()
                    .map(item -> item.getTheGroup().getId()).collect(Collectors.toList());
            theGroupList = theGroupService.findAllByIdIn(listId);
        }
        return new ResponseEntity<>(theGroupList, HttpStatus.OK);
    }

    @GetMapping("/findAllGroup")
    public ResponseEntity<Object> findAllGroup(@RequestParam Long idUser) {
        List<TheGroup> theGroups = theGroupService.findAllGroup(idUser);
        if (CollectionUtils.isEmpty(theGroups)) {
            theGroups = new ArrayList<>();
        }
        return new ResponseEntity<>(theGroups, HttpStatus.OK);
    }

    // Danh sách những người đăng kí tham gia nhóm chưa được duyệt
    @GetMapping("/findAllUserStatusPendingApproval")
    public ResponseEntity<Object> findAllGroupParticipantByTheGroupId(@RequestParam Long idTheGroup) {
        List<GroupParticipant> groupParticipants = groupParticipantService.findAllUserStatusPendingApproval(idTheGroup);
        if (CollectionUtils.isEmpty(groupParticipants)) {
            groupParticipants = new ArrayList<>();
        }
        return new ResponseEntity<>(groupParticipants, HttpStatus.OK);
    }

    // Danh sách những người trong nhóm
    @GetMapping("/findAllUserStatusApproved")
    public ResponseEntity<Object> findAllUserStatusApproved(@RequestParam Long idTheGroup) {
        List<GroupParticipant> groupParticipants = groupParticipantService.findAllUserStatusApproved(idTheGroup);
        if (CollectionUtils.isEmpty(groupParticipants)) {
            groupParticipants = new ArrayList<>();
        }
        return new ResponseEntity<>(groupParticipants, HttpStatus.OK);
    }

    // Danh sách nhóm bởi người tạo
    @GetMapping("/findGroupByIdUserCreate")
    public ResponseEntity<Object> findGroupByIdUserCreate(@RequestParam Long idUserCreate) {
        List<TheGroup> listGroupByIdUserCreate = theGroupService.findByIdUserCreate(idUserCreate);
        if (CollectionUtils.isEmpty(listGroupByIdUserCreate)) {
            listGroupByIdUserCreate = new ArrayList<>();
        } else {
            List<Long> ids = listGroupByIdUserCreate.stream().map(TheGroup::getId).collect(Collectors.toList());
            List<GroupParticipant> groupParticipants = groupParticipantRepository.findAllUserStatusApprovedInList(ids);
            for (int i = 0; i < listGroupByIdUserCreate.size(); i++) {
                Long id = listGroupByIdUserCreate.get(i).getId();
                List<GroupParticipant> groupParticipantList = groupParticipants
                        .stream().filter(item -> item.getTheGroup().getId().equals(id)).toList();
                listGroupByIdUserCreate.get(i).setNumberUser((long) groupParticipantList.size());
            }
        }
        return new ResponseEntity<>(listGroupByIdUserCreate, HttpStatus.OK);
    }

    @GetMapping("/findById")
    public ResponseEntity<Object> findById(@RequestParam Long idGroup) {
        Optional<TheGroup> theGroupOptional = theGroupService.findById(idGroup);
        if (theGroupOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_GROUP, idGroup),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(theGroupOptional.get(), HttpStatus.OK);
    }

    // Tạo nhóm mới
    @PostMapping("/createGroup")
    public ResponseEntity<Object> createGroup(@RequestBody TheGroup theGroup, @RequestParam Long idUser) {
        User user = userService.checkExistUser(idUser);
        if (StringUtils.isEmpty(theGroup.getGroupName())) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField("groupName"),
                    HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(theGroup.getType())) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField("type"),
                    HttpStatus.BAD_REQUEST);
        }
        Common.handlerWordsLanguage(theGroup);
        theGroup.setCreateBy(user.getFullName());
        theGroup.setIdUserCreate(user.getId());
        theGroup.setCreateAt(new Date());
        theGroup.setNumberUser(0L);
        theGroup.setStatus(Constants.STATUS_PUBLIC);
        if (StringUtils.isEmpty(theGroup.getAvatarGroup())) {
            theGroup.setAvatarGroup(Constants.ImageDefault.DEFAULT_AVATAR_GROUP);
        }
        if (StringUtils.isEmpty(theGroup.getCoverGroup())) {
            theGroup.setCoverGroup(Constants.ImageDefault.DEFAULT_COVER_GROUP);
        }
        theGroupService.save(theGroup);
        ImageGroup coverGroup = imageGroupService
                .createImageGroupDefault(theGroup.getCoverGroup(), theGroup.getId(), user.getId());
        ImageGroup avatarGroup = imageGroupService
                .createImageGroupDefault(theGroup.getAvatarGroup(), theGroup.getId(), user.getId());
        imageGroupService.save(coverGroup);
        imageGroupService.save(avatarGroup);
        GroupParticipant groupParticipant = groupParticipantService
                .createDefault(theGroup, user, Constants.GroupStatus.MANAGEMENT);
        groupParticipantService.save(groupParticipant);
        return new ResponseEntity<>(theGroup, HttpStatus.OK);
    }

    // Khóa nhóm, xóa nhóm
    @DeleteMapping("/actionGroup")
    public ResponseEntity<Object> lockGroup(@RequestParam Long idGroup,
                                            @RequestParam Long idUser,
                                            @RequestParam String type) {
        Optional<TheGroup> theGroupOptional = theGroupService.findById(idGroup);
        if (theGroupOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_GROUP, idGroup),
                    HttpStatus.NOT_FOUND);
        }
        User user = userService.checkExistUser(idUser);
        if ("lock".equals(type)) {
            if (userService.checkAdmin(idUser).toString().substring(17, 27).equals(Constants.Roles.ROLE_ADMIN)) {
                theGroupOptional.get().setStatus(Constants.STATUS_LOCK);
                theGroupService.save(theGroupOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            if (theGroupOptional.get().getIdUserCreate().equals(user.getId())) {
                theGroupOptional.get().setStatus(Constants.STATUS_DELETE);
                theGroupService.save(theGroupOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        if ("delete".equals(type)) {
            if (theGroupOptional.get().getIdUserCreate().equals(user.getId())) {
                theGroupOptional.get().setStatus(Constants.STATUS_DELETE);
                theGroupService.save(theGroupOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    // Gửi yêu cầu tham gia nhóm
    @GetMapping("/createGroupParticipant")
    public ResponseEntity<Object> createGroupParticipant(@RequestParam Long idUser, @RequestParam Long idTheGroup) {
        Optional<TheGroup> theGroupOptional = theGroupService.findById(idTheGroup);
        if (theGroupOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_THE_GROUP, idTheGroup),
                    HttpStatus.NOT_FOUND);
        }
        User userSendRequest = userService.checkExistUser(idUser);
        User owner = userService.checkExistUser(theGroupOptional.get().getIdUserCreate());
        Iterable<GroupParticipant> groupParticipants = groupParticipantService.findAll();
        List<GroupParticipant> groupParticipantList = (List<GroupParticipant>) groupParticipants;
        for (GroupParticipant groupParticipant : groupParticipantList) {
            if (groupParticipant.getUser().getId().equals(idUser)
                    && groupParticipant.getTheGroup().getId().equals(idTheGroup)) {
                return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                        MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                        HttpStatus.BAD_REQUEST);
            }
        }
        GroupParticipant groupParticipant = groupParticipantService
                .createDefault(theGroupOptional.get(), userSendRequest, Constants.GroupStatus.STATUS_GROUP_PENDING);
        groupParticipantService.save(groupParticipant);
        Notification notification = notificationService.createDefault(owner, userSendRequest,
                Constants.Notification.TITLE_REQUEST_JOIN_GROUP, groupParticipant.getId(),
                Constants.Notification.TYPE_GROUP);
        notificationService.save(notification);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Quản trị viên đồng ý, từ chối user tham gia nhóm
    @GetMapping("/actionJoinGroup")
    public ResponseEntity<Object> actionJoinGroup(@RequestParam Long idAdminGroup,
                                                  @RequestParam Long idUser,
                                                  @RequestParam Long idGroup,
                                                  @RequestParam String type) {
        User adminGroup = userService.checkExistUser(idAdminGroup);
        Optional<GroupParticipant> groupParticipant = groupParticipantService.findByUserIdAndTheGroupId(idUser, idGroup);
        if (groupParticipant.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userService.checkExistUser(idUser);
        if (Constants.ACCEPT.equals(type)) {
            if (adminGroup.getId().equals(groupParticipant.get().getTheGroup().getIdUserCreate())) {
                groupParticipant.get().setStatus(Constants.GroupStatus.STATUS_GROUP_APPROVED);
                groupParticipantService.save(groupParticipant.get());
                Notification notification = notificationService.createDefault(user, adminGroup,
                        Constants.Notification.TITLE_APPROVE_JOIN_GROUP, groupParticipant.get().getId(),
                        Constants.Notification.TYPE_GROUP);
                notificationService.save(notification);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        if (Constants.REJECT.equals(type)) {
            if (adminGroup.getId().equals(groupParticipant.get().getTheGroup().getIdUserCreate())) {
                groupParticipant.get().setStatus(Constants.GroupStatus.STATUS_GROUP_REFUSE);
                groupParticipantService.save(groupParticipant.get());
                Notification notification = notificationService.createDefault(user, adminGroup,
                        Constants.Notification.TITLE_REJECT_JOIN_GROUP, groupParticipant.get().getId(),
                        Constants.Notification.TYPE_GROUP);
                notificationService.save(notification);
                return new ResponseEntity<>(groupParticipant, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/check")
    public ResponseEntity<Object> check(@RequestParam Long idUser, @RequestParam Long idGroup) {
        Optional<GroupParticipant> groupParticipant = groupParticipantService.findByUserIdAndTheGroupId(idUser, idGroup);
        if (groupParticipant.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(groupParticipant, HttpStatus.OK);
    }

    // Quản trị viên duyệt, từ chối duyệt bài viết
    @GetMapping("/actionUserUpPost")
    public ResponseEntity<Object> actionUserUpPost(@RequestParam Long idAdminGroup,
                                                   @RequestParam Long idGroupPost,
                                                   @RequestParam String type) {
        User adminGroup = userService.checkExistUser(idAdminGroup);
        Optional<GroupPost> groupPost = groupPostService.findById(idGroupPost);
        if (groupPost.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_THE_GROUP_POST,
                    idGroupPost), HttpStatus.NOT_FOUND);
        }
        if (Constants.ACCEPT.equals(type)) {
            groupPost.get().setStatus(Constants.GroupStatus.STATUS_GROUP_APPROVED);
            groupPostService.save(groupPost.get());
            Notification notification = notificationService.createDefault(groupPost.get().getUser(), adminGroup,
                    Constants.Notification.TITLE_APPROVE, idGroupPost, Constants.Notification.TYPE_GROUP_POST);
            notificationService.save(notification);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if (Constants.REJECT.equals(type)) {
            groupPost.get().setStatus(Constants.GroupStatus.STATUS_GROUP_REFUSE);
            groupPostService.save(groupPost.get());
            Notification notification = notificationService.createDefault(groupPost.get().getUser(), adminGroup,
                    Constants.Notification.TITLE_REJECT, idGroupPost, Constants.Notification.TYPE_GROUP_POST);
            notificationService.save(notification);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/deleteGroupPost")
    public ResponseEntity<Object> deleteGroupPost(@RequestParam Long idUser,
                                                  @RequestParam Long idGroupPost) {
        userService.checkExistUser(idUser);
        Optional<GroupPost> groupPost = groupPostService.findById(idGroupPost);
        if (groupPost.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_THE_GROUP_POST,
                    idGroupPost), HttpStatus.NOT_FOUND);
        }
        if (groupPost.get().getUser().getId().equals(idUser)
                || groupPost.get().getTheGroup().getIdUserCreate().equals(idUser)) {
            groupPostService.deleteGroupPost(groupPost.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Tạo bài viết trong nhóm
    @PostMapping("/createGroupPost")
    public ResponseEntity<Object> createGroupPost(@RequestParam Long idUser,
                                                  @RequestParam Long idTheGroup,
                                                  @RequestBody GroupPost groupPost) {
        if (StringUtils.isEmpty(groupPost.getContent().trim())) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField(Constants.DataField.CONTENT),
                    HttpStatus.BAD_REQUEST);
        }
        Common.handlerWordsLanguage(groupPost);
        User user = userService.checkExistUser(idUser);
        Optional<TheGroup> theGroupOptional = theGroupService.findById(idTheGroup);
        if (theGroupOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_THE_GROUP, idTheGroup), HttpStatus.NOT_FOUND);
        }
        groupPost.setStatus(Constants.GroupStatus.STATUS_GROUP_PENDING);
        groupPost.setCreateBy(user.getFullName());
        groupPost.setCreateAt(new Date());
        groupPost.setTheGroup(theGroupOptional.get());
        groupPost.setUser(user);
        if (theGroupOptional.get().getIdUserCreate().equals(idUser)) {
            groupPost.setStatus(Constants.GroupStatus.STATUS_GROUP_APPROVED);
        }
        groupPostService.save(groupPost);
        if (!StringUtils.isEmpty(groupPost.getImage())) {
            ImageGroup imageGroup = imageGroupService.createImageGroupDefault(groupPost.getImage(),
                    theGroupOptional.get().getId(), user.getId());
            imageGroupService.save(imageGroup);
        }
        User adminGroup = userService.checkExistUser(theGroupOptional.get().getIdUserCreate());
        Notification notification = notificationService.createDefault(adminGroup, user,
                Constants.Notification.TITLE_REQUEST_CREATE_POST, idTheGroup, Constants.Notification.TYPE_GROUP_POST);
        notificationService.save(notification);
        return new ResponseEntity<>(groupPost, HttpStatus.OK);
    }

    // Các bài viết trong nhóm
    @GetMapping("/postByGroup")
    public ResponseEntity<Object> postByGroup(@RequestParam Long idGroup, @RequestParam String type) {
        Optional<TheGroup> theGroupOptional = theGroupService.findById(idGroup);
        if (theGroupOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_THE_GROUP, idGroup), HttpStatus.NOT_FOUND);
        }
        List<GroupPost> groupPostList = new ArrayList<>();
        if (type.equals(Constants.GroupStatus.STATUS_GROUP_APPROVED)) {
            groupPostList = groupPostService.findAllPostByIdGroup(idGroup);
        }
        if (type.equals(Constants.GroupStatus.STATUS_GROUP_PENDING)) {
            groupPostList = groupPostService.findAllPostWaiting(idGroup);
        }
        return new ResponseEntity<>(groupPostList, HttpStatus.OK);
    }

    @GetMapping("/checkUserInGroup")
    public ResponseEntity<Object> checkUserInGroup(@RequestParam Long idGroup, @RequestParam Long idUser) {
        Optional<TheGroup> theGroupOptional = theGroupService.findById(idGroup);
        if (theGroupOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_THE_GROUP, idGroup), HttpStatus.NOT_FOUND);
        }
        userService.checkExistUser(idUser);
        List<GroupParticipant> groupParticipants = groupParticipantService.findAllUserStatusApproved(idGroup);
        List<GroupParticipant> groupParticipantList = groupParticipants.stream()
                .filter(item -> item.getUser().getId()
                        .equals(idUser) && item.getStatus()
                        .equals(Constants.GroupStatus.STATUS_GROUP_APPROVED))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(groupParticipantList)) {
            groupParticipantList = new ArrayList<>();
        }
        return new ResponseEntity<>(groupParticipantList, HttpStatus.OK);
    }

    @GetMapping("/searchAllByGroupNameAndType")
    public ResponseEntity<Object> searchAllByGroupNameAndType(String search, @RequestParam Long idUser) {
        userService.checkExistUser(idUser);
        search = Common.addEscapeOnSpecialCharactersWhenSearch(search);
        List<TheGroup> theGroupList = theGroupService.searchAllByGroupNameAndType(search, idUser);
        return new ResponseEntity<>(theGroupList, HttpStatus.OK);
    }
}
