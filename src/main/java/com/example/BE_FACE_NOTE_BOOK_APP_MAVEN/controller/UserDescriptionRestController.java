package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Common;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.UserDescription;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.UserDescriptionService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/descriptions")
@Slf4j
public class UserDescriptionRestController {

    private final UserDescriptionService userDescriptionService;

    private final UserService userService;

    @Autowired
    public UserDescriptionRestController(UserDescriptionService userDescriptionService,
                                         UserService userService) {
        this.userDescriptionService = userDescriptionService;
        this.userService = userService;
    }

    @GetMapping("/getDescriptionByIdUser")
    public ResponseEntity<Object> getDescriptionByIdUser(@RequestParam Long idUser) {
        List<UserDescription> list = userDescriptionService.findAllByUserId(idUser);
        if (CollectionUtils.isEmpty(list)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(list.get(0), HttpStatus.OK);
    }

    // Tạo mới mô tả
    @PostMapping("/createDescription")
    public ResponseEntity<Object> createDescription(@RequestBody UserDescription userDescription,
                                               @RequestParam Long idUser) {
        if (StringUtils.isEmpty(userDescription.getDescription())) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Common.handlerWordsLanguage(userDescription);
        User user = userService.checkExistUser(idUser);
        userDescription.setUser(user);
        userDescription.setCreateAt(new Date());
        userDescription.setStatus(Constants.STATUS_PUBLIC);
        userDescriptionService.save(userDescription);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Sửa mô tả
    @PutMapping("/editDescription")
    public ResponseEntity<Object> editDescription(@RequestBody UserDescription userDescription,
                                             @RequestParam Long idUserDescription) {
        if (StringUtils.isEmpty(userDescription.getDescription())) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Common.handlerWordsLanguage(userDescription);
        Optional<UserDescription> description = userDescriptionService.findById(idUserDescription);
        if (description.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER_DESCRIPTION, idUserDescription), HttpStatus.NOT_FOUND);
        }
        description.get().setEditAt(new Date());
        description.get().setDescription(userDescription.getDescription());
        userDescriptionService.save(description.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Xóa mô tả
    @DeleteMapping("/deleteDescription")
    public ResponseEntity<Object> deleteDescription(@RequestParam Long idUser) {
        List<UserDescription> list = userDescriptionService.findAllByUserId(idUser);
        if (!CollectionUtils.isEmpty(list)) userDescriptionService.delete(list.get(0));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
