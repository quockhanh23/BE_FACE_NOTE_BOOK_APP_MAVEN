package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.NotificationDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.UserNotificationDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Notification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.NotificationService;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    private final ModelMapper modelMapper;

    @Autowired
    public NotificationController(NotificationService notificationService,
                                  ModelMapper modelMapper) {
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/getAllNotificationByIdSenTo")
    public ResponseEntity<?> getAllNotificationByIdSenTo(@RequestParam Long idSenTo) {
        List<Notification> notificationList = notificationService.findAllByIdSendTo(idSenTo);
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(notificationList)) {
            for (Notification notification : notificationList) {
                NotificationDTO notificationDTO = modelMapper.map(notification, NotificationDTO.class);
                UserNotificationDTO userAction = modelMapper.map(notification.getIdAction(), UserNotificationDTO.class);
                UserNotificationDTO userSendTo = modelMapper.map(notification.getIdSendTo(), UserNotificationDTO.class);
                notificationDTO.setIdAction(userAction);
                notificationDTO.setIdSendTo(userSendTo);
                notificationDTOS.add(notificationDTO);
            }
        }
        log.info("List Notification by user: {}", notificationDTOS);
        return new ResponseEntity<>(notificationDTOS, HttpStatus.OK);
    }

    @GetMapping("/findAllByIdSendToNotSeen")
    public ResponseEntity<?> findAllByIdSendToNotSeen(@RequestParam Long idSenTo) {
        List<Notification> notificationList = notificationService.findAllByIdSendToNotSeen(idSenTo);
        return new ResponseEntity<>(notificationList.size(), HttpStatus.OK);
    }

    // Đã xem tất cả thông báo
    @GetMapping("/seenAll")
    public ResponseEntity<?> seenAll(@RequestParam Long idSenTo) {
        List<Notification> notificationList = notificationService.findAllByIdSendTo(idSenTo);
        if (!CollectionUtils.isEmpty(notificationList)) {
            notificationList.forEach(notification -> notification.setStatus(Constants.Notification.STATUS_SEEN));
            notificationService.saveAll(notificationList);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Xóa tất cả thông báo
    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAll(@RequestParam Long idSenTo) {
        List<Notification> notificationList = notificationService.findAllByIdSendTo(idSenTo);
        if (!CollectionUtils.isEmpty(notificationList)) {
            notificationService.deleteAll(notificationList);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
