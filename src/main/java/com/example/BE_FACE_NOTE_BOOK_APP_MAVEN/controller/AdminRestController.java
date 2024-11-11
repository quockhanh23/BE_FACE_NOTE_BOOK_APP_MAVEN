package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Common;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.MessageResponse;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.GroupPostDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.PostCheck;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.UserDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.*;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.GroupPostRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.ReportRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.TheGroupRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.PostService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/admins")
@Slf4j
public class AdminRestController {

    private final UserService userService;

    private final PostService postService;

    private final TheGroupRepository theGroupRepository;

    private final GroupPostRepository groupPostRepository;

    private final ReportRepository reportRepository;

    @Autowired
    public AdminRestController(UserService userService, PostService postService,
                               TheGroupRepository theGroupRepository,
                               GroupPostRepository groupPostRepository,
                               ReportRepository reportRepository) {
        this.userService = userService;
        this.postService = postService;
        this.theGroupRepository = theGroupRepository;
        this.groupPostRepository = groupPostRepository;
        this.reportRepository = reportRepository;
    }

    @GetMapping("/adminAction")
    public ResponseEntity<Object> adminAction(@RequestParam Long idAdmin,
                                              @RequestParam String type,
                                              @SuppressWarnings("unused")
                                              @RequestHeader("Authorization") String authorization) {
        if ("user".equals(type)) {
            List<User> users = userService.findAllRoleUser();
            List<UserDTO> userDTOList = userService.copyListDTO(users);
            return new ResponseEntity<>(userDTOList, HttpStatus.OK);
        }
        if ("post".equals(type)) {
            Iterable<Post2> post2List = postService.findAll();
            List<Post2> list = (List<Post2>) post2List;
            List<PostCheck> postChecks = new ArrayList<>();
            list.forEach(post2 -> {
                PostCheck postCheck = new PostCheck();
                BeanUtils.copyProperties(post2, postCheck);
                postCheck.setIdUser(post2.getUser().getId());
                postCheck.setFullName(post2.getUser().getFullName());
                postChecks.add(postCheck);
            });
            postChecks.sort((p1, p2) -> (p2.getCreateAt().compareTo(p1.getCreateAt())));
            return new ResponseEntity<>(postChecks, HttpStatus.OK);
        }
        if ("group".equals(type)) {
            List<TheGroup> theGroupList = theGroupRepository.findAll();
            if (CollectionUtils.isEmpty(theGroupList)) {
                theGroupList = new ArrayList<>();
            }
            return new ResponseEntity<>(theGroupList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAllGroupPost")
    public ResponseEntity<Object> getAllGroupPost(@RequestParam Long idGroup, @RequestParam Long idAdmin,
                                                  @SuppressWarnings("unused")
                                                  @RequestHeader("Authorization") String authorization) {
        List<GroupPost> list = groupPostRepository.findAllByTheGroupId(idGroup);
        List<GroupPostDTO> groupPostDTOList = new ArrayList<>();
        for (GroupPost groupPost : list) {
            GroupPostDTO groupPostDTO = new GroupPostDTO();
            BeanUtils.copyProperties(groupPostDTO, groupPost);
            groupPostDTO.setGroupName(groupPost.getTheGroup().getGroupName());
            groupPostDTO.setUserName(groupPost.getUser().getUsername());
            groupPostDTOList.add(groupPostDTO);
        }
        return new ResponseEntity<>(groupPostDTOList, HttpStatus.OK);
    }

    @GetMapping("/findById")
    public ResponseEntity<Object> findById(@RequestParam Long idUser) {
        User user = userService.checkExistUser(idUser);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        List<ReportViolations> violations = reportRepository
                .findAllByIdViolateAndType(user.getId(), Constants.REPOST_TYPE_USER);
        userDTO.setNumberRepost(violations.size());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @DeleteMapping("/actionUser")
    public ResponseEntity<Object> actionUser(@RequestParam Long idAdmin,
                                             @RequestParam Long idUser,
                                             @RequestParam String type,
                                             @SuppressWarnings("unused")
                                             @RequestHeader("Authorization") String authorization) {
        User user = userService.checkExistUser(idUser);
        if ("baned".equals(type)) {
            user.setStatus(Constants.STATUS_BANED);
            userService.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if ("active".equals(type)) {
            user.setStatus(Constants.STATUS_ACTIVE);
            userService.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/actionPost")
    public ResponseEntity<Object> actionPost(@RequestParam Long idAdmin,
                                             @RequestParam Long idPost,
                                             @RequestParam String type,
                                             // Sử dụng param này bên Aspect
                                             @SuppressWarnings("unused")
                                             @RequestHeader("Authorization") String authorization) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_POST, idPost), HttpStatus.NOT_FOUND);
        }
        if ("delete".equals(type)) {
            postService.delete(postOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if ("lock".equals(type)) {
            postOptional.get().setStatus(Constants.STATUS_PRIVATE);
            postService.save(postOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/searchAllPeople")
    public ResponseEntity<Object> searchAllPeople(@RequestParam Long idUser,
                                                  @RequestParam(required = false) String searchText,
                                                  @SuppressWarnings("unused")
                                                  @RequestHeader("Authorization") String authorization) {
        searchText = Common.addEscapeOnSpecialCharactersWhenSearch(searchText);
        List<User> users = userService.findAllByEmailOrUsername(searchText);
        List<UserDTO> userDTOList = userService.copyListDTO(users);
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }
}
