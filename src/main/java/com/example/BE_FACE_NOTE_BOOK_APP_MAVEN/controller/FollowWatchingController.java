package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.UserDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.FollowWatchingService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/follows")
@Slf4j
public class FollowWatchingController {

    private final UserService userService;

    private final FollowWatchingService followWatchingService;

    @Autowired
    public FollowWatchingController(UserService userService,
                                    FollowWatchingService followWatchingService) {
        this.userService = userService;
        this.followWatchingService = followWatchingService;
    }

    @GetMapping("/getListFollowByIdUser")
    public ResponseEntity<Object> getListFollowByIdUser(@RequestParam Long idUser) {
        List<UserDTO> userList = userService.filterUser(idUser, Constants.FollowPeople.FOLLOW);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/getListWatchingByIdUser")
    public ResponseEntity<Object> getListWatchingByIdUser(@RequestParam Long idUser) {
        List<UserDTO> userList = userService.filterUser(idUser, Constants.FollowPeople.WATCHING);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/follow")
    public ResponseEntity<Object> createFollow(@RequestParam Long idUserLogin, @RequestParam Long idUserFollow) {
        followWatchingService.createFollow(idUserLogin, idUserFollow);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/unFollow")
    public ResponseEntity<Object> unFollow(@RequestParam Long idUserLogin, @RequestParam Long idUserFollow) {
        followWatchingService.unFollow(idUserLogin, idUserFollow);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getOne")
    public ResponseEntity<Object> getOne(@RequestParam Long idUserLogin, @RequestParam Long idUserFollow) {
        Object user = followWatchingService.checkUserHadFollow(idUserLogin, idUserFollow);
        if (Objects.nonNull(user)) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
