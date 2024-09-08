package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Common;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.MessageResponse;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.*;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.*;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.ConversationDeleteTimeRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.ConversationService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.MessengerService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.NotificationService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/conversations")
@Slf4j
public class ConversationRestController {

    private final ConversationService conversationService;

    private final MessengerService messengerService;

    private final UserService userService;

    private final ConversationDeleteTimeRepository conversationDeleteTimeRepository;

    private final ModelMapper modelMapper;

    private final NotificationService notificationService;

    @Autowired
    public ConversationRestController(ConversationService conversationService,
                                      MessengerService messengerService,
                                      UserService userService,
                                      ConversationDeleteTimeRepository conversationDeleteTimeRepository,
                                      ModelMapper modelMapper,
                                      NotificationService notificationService) {
        this.conversationService = conversationService;
        this.messengerService = messengerService;
        this.userService = userService;
        this.conversationDeleteTimeRepository = conversationDeleteTimeRepository;
        this.modelMapper = modelMapper;
        this.notificationService = notificationService;
    }

    //   messengers2.sort((p1, p2) -> p2.getCount() - p1.getCount());
    // Tạo cuộc trò truyện
    @GetMapping("/createConversation")
    public ResponseEntity<?> createConversation(@RequestParam Long idSender,
                                                @RequestParam Long idReceiver) {
        Optional<User> userSender = userService.findById(idSender);
        if (userSender.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_SENDER, idSender), HttpStatus.NOT_FOUND);
        }
        Optional<User> userReceiver = userService.findById(idReceiver);
        if (userReceiver.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_RECEIVER, idReceiver), HttpStatus.NOT_FOUND);
        }
        List<Conversation> conversationList = conversationService.getConversationBySenderIdOrReceiverId(idSender, idReceiver);
        if (!CollectionUtils.isEmpty(conversationList)) {
            for (Conversation conversation : conversationList) {
                if ((conversation.getIdSender().getId().equals(idSender)
                        && conversation.getIdReceiver().getId().equals(idReceiver))
                        || (conversation.getIdSender().getId().equals(idReceiver)
                        && conversation.getIdReceiver().getId().equals(idSender))) {
                    return new ResponseEntity<>(conversation, HttpStatus.OK);
                }
            }
        }
        Conversation conversation = new Conversation();
        conversation.setIdSender(userSender.get());
        conversation.setIdReceiver(userReceiver.get());
        conversation.setCreateAt(new Date());
        conversationService.save(conversation);
        return new ResponseEntity<>(conversation, HttpStatus.OK);
    }

    @GetMapping("/searchMessage")
    public ResponseEntity<?> searchMessage(String search, @RequestParam Long idConversation) {
        search = Common.addEscapeOnSpecialCharactersWhenSearch(search);
        List<Messenger> messengers = messengerService.searchMessage(search, idConversation);
        return new ResponseEntity<>(messengers, HttpStatus.OK);
    }

    @DeleteMapping("/deleteOneSide")
    public ResponseEntity<?> deleteOneSide(@RequestParam Long idUser, @RequestParam Long idConversation) {
        boolean checkPresent = conversationService.existsConversationsById(idConversation);
        if (!checkPresent) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_CONVERSATION, idConversation), HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        ConversationDeleteTime conversationDeleteTime = new ConversationDeleteTime();
        conversationDeleteTime.setIdDelete(idUser);
        conversationDeleteTime.setIdConversation(idConversation);
        conversationDeleteTime.setTimeDelete(new Date());
        conversationDeleteTimeRepository.save(conversationDeleteTime);
        List<Messenger> messengers = messengerService.findAllByConversation_Id(idConversation);
        for (Messenger messenger : messengers) {
            if (messenger.getConversationDeleteTime().getId() == null) {
                messenger.setConversationDeleteTime(conversationDeleteTime);
            }
        }
        messengerService.saveAll(messengers);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/myMessenger")
    public ResponseEntity<?> myMessenger(@RequestParam Long idUser) {
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        List<Conversation> conversations = conversationService.findAllByIdSender(userOptional.get().getId());
        if (CollectionUtils.isEmpty(conversations)) {
            conversations = new ArrayList<>();
        }
        List<ConversationDTO> conversationDTOS = new ArrayList<>();
        for (Conversation conversation : conversations) {
            UserDTO userSender = new UserDTO();
            BeanUtils.copyProperties(conversation.getIdSender(), userSender);
            UserDTO userReceiver = new UserDTO();
            BeanUtils.copyProperties(conversation.getIdReceiver(), userReceiver);
            ConversationDTO conversationDTO = new ConversationDTO();
            BeanUtils.copyProperties(conversation, conversationDTO);
            conversationDTO.setIdSender(userSender);
            conversationDTO.setIdReceiver(userReceiver);
            conversationDTOS.add(conversationDTO);
        }
        return new ResponseEntity<>(conversationDTOS, HttpStatus.OK);
    }

    @GetMapping("/messenger")
    public ResponseEntity<?> messenger(@RequestParam Long idUser, @RequestParam Long idConversation) {
        Optional<Conversation> conversation = conversationService.findById(idConversation);
        if (conversation.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_CONVERSATION, idConversation), HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        List<Messenger> messengers = messengerService.findAllByConversationOrderById(conversation.get());
        if (CollectionUtils.isEmpty(messengers)) {
            messengers = new ArrayList<>();
        }
        return new ResponseEntity<>(messengers, HttpStatus.OK);
    }

    // Tạo tin nhắn
    @PostMapping("/createMessengers")
    public ResponseEntity<?> createMessengers(@RequestParam Long idConversation,
                                              @RequestParam Long idUser,
                                              @RequestBody Messenger messenger) {
        Optional<Conversation> conversation = conversationService.findById(idConversation);
        if (conversation.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_CONVERSATION, idConversation), HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if (StringUtils.isEmpty(messenger.getContent()) && StringUtils.isEmpty(messenger.getImage())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        messenger.setConversation(conversation.get());
        if (!Objects.equals(messenger.getConversation().getIdSender().getId(), userOptional.get().getId())
                && (messenger.getConversation().getIdReceiver().getId().equals(userOptional.get().getId()))) {
            Messenger message = messengerService.createDefaultMessage(messenger, userOptional.get(),
                    Constants.RESPONSE, Constants.ConversationStatus.STATUS_TWO);
            messengerService.save(message);
            Notification notification = notificationService.createDefault(messenger.getConversation().getIdSender(),
                    messenger.getConversation().getIdReceiver(),
                    Constants.Notification.TITLE_SEND_MESSAGE, idConversation,
                    Constants.Notification.TYPE_CONVERSATION);
            notificationService.save(notification);
            return new ResponseEntity<>(messenger, HttpStatus.OK);
        }
        if (!Objects.equals(messenger.getConversation().getIdReceiver().getId(), userOptional.get().getId())
                && messenger.getConversation().getIdSender().getId().equals(userOptional.get().getId())) {
            Messenger message = messengerService.createDefaultMessage(messenger, userOptional.get(),
                    Constants.REQUEST, Constants.ConversationStatus.STATUS_TWO);
            messengerService.save(message);
            Notification notification = notificationService.createDefault(messenger.getConversation().getIdReceiver(),
                    messenger.getConversation().getIdSender(),
                    Constants.Notification.TITLE_SEND_MESSAGE, idConversation,
                    Constants.Notification.TYPE_CONVERSATION);
            notificationService.save(notification);
            return new ResponseEntity<>(messenger, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long idConversation) {
        Optional<Conversation> conversation = conversationService.findById(idConversation);
        if (conversation.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_CONVERSATION, idConversation), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(conversation, HttpStatus.OK);
    }

    @GetMapping("/findAllByConversationOrderById")
    public ResponseEntity<?> findAllByConversationOrderById(@RequestParam Long idConversation) {
        Optional<Conversation> conversation = conversationService.findById(idConversation);
        if (conversation.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_CONVERSATION, idConversation), HttpStatus.NOT_FOUND);
        }
        List<Messenger> messengers = messengerService.findAllByConversationOrderById(conversation.get());
        if (CollectionUtils.isEmpty(messengers)) {
            messengers = new ArrayList<>();
        }
        return new ResponseEntity<>(messengers, HttpStatus.OK);
    }

    // Các ảnh đã gửi ở cuộc trò truyện
    @GetMapping("/getAllMessageHavePhoto")
    public ResponseEntity<?> getAllMessageHavePhoto(@RequestParam Long idConversation) {
        boolean checkPresent = conversationService.existsConversationsById(idConversation);
        if (!checkPresent) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_CONVERSATION, idConversation), HttpStatus.NOT_FOUND);
        }
        List<MessagePhotoDTO> messengersHaveLink = new ArrayList<>();
        List<Messenger> messengers = messengerService.findImageByConversation(idConversation);
        if (!CollectionUtils.isEmpty(messengers)) {
            for (Messenger messenger : messengers) {
                MessagePhotoDTO messengerDTO = modelMapper.map(messenger, MessagePhotoDTO.class);
                messengersHaveLink.add(messengerDTO);
            }
        }
        return new ResponseEntity<>(messengersHaveLink, HttpStatus.OK);
    }

    // Xem link đã gửi ở cuộc trò truyện
    @GetMapping("/getAllMessageHaveLink")
    public ResponseEntity<?> getAllMessageHaveLink(@RequestParam Long idConversation) {
        Set<String> list = new HashSet<>();
        List<Messenger> messengers = messengerService.findAllByConversation_IdAndContentNotNullOrderByIdDesc(idConversation);
        if (!CollectionUtils.isEmpty(messengers)) {
            list = messengers.stream()
                    .filter(i -> i.getContent().length() > 8
                            && (i.getContent().substring(0, 8).equals(Constants.Link.CHECK_LINK)
                            || i.getContent().substring(0, 5).equals(Constants.Link.CHECK_LINK_2)))
                    .map(Messenger::getContent).collect(Collectors.toSet());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // Nhắn tin với người không có trong danh sách bạn bè
    @GetMapping("/listConversationNotFriend")
    public ResponseEntity<?> listConversationNotFriend(@RequestParam Long idUser) {
        List<Conversation> conversationList = conversationService.listConversationByIdUserNotFriend(idUser);
        List<Conversation> conversations = new ArrayList<>();
        if (!CollectionUtils.isEmpty(conversationList)) {
            List<Long> listId = conversationList.stream().map(Conversation::getId).collect(Collectors.toList());
            List<Messenger> messengers = messengerService.findAllByConversation_IdIn(listId);
            for (Long id : listId) {
                List<Messenger> messengerList = messengers.stream()
                        .filter(item -> item.getConversation().getId().equals(id)).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(messengerList)) {
                    conversationList.stream()
                            .filter(item -> item.getId().equals(id)).findFirst().ifPresent(conversations::add);
                }
            }
        }
        return new ResponseEntity<>(conversations, HttpStatus.OK);
    }

    @GetMapping("/messageNotFriend")
    public ResponseEntity<?> messageNotFriend(@RequestParam Long idConversation) {
        List<Messenger> messengers = messengerService.findAllByConversation_Id(idConversation);
        List<MessengerDTO> messengerList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(messengers)) {
            for (Messenger messenger : messengers) {
                MessengerDTO messengerDTO = new MessengerDTO();
                BeanUtils.copyProperties(messenger, messengerDTO);
                messengerDTO.setIdConversation(messenger.getConversation().getId());
                messengerList.add(messengerDTO);
            }
        }
        return new ResponseEntity<>(messengerList, HttpStatus.OK);
    }

    // Id của người gửi tin nhắn cuối
    @GetMapping("/lastMessageIdSender")
    public ResponseEntity<?> lastMessage(@RequestParam Long idConversation) {
        Messenger messengers = messengerService.lastMessage(idConversation);
        if (Objects.nonNull(messengers)) {
            return new ResponseEntity<>(messengers.getIdSender().getId(), HttpStatus.OK);
        }
        return null;
    }

    // Tin nhắn cuối cùng
    @GetMapping("/lastMessage")
    public ResponseEntity<?> lastMessageTime(@RequestParam Long idConversation) {
        Object lastTimeMessage = messengerService.lastTimeMessage(idConversation);
        if (Objects.nonNull(lastTimeMessage)) {
            return new ResponseEntity<>(lastTimeMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Date(), HttpStatus.OK);
        }
    }

    @GetMapping("/testConverterJPA")
    public ResponseEntity<?> testConverterJPA(@RequestParam Long idConversation) {
        List<MessengerDTO2> objects = messengerService.testConverterJPA(idConversation);
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }

    // Xóa 1 tin nhắn
    @DeleteMapping("/deleteMessenger")
    public ResponseEntity<?> deleteMessenger(@RequestParam Long idUser,
                                             @RequestParam Long idConversation,
                                             @RequestParam Long idMessenger) {
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<Conversation> conversation = conversationService.findById(idConversation);
        if (conversation.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_CONVERSATION, idConversation), HttpStatus.NOT_FOUND);
        }
        Optional<Messenger> messenger = messengerService.findById(idMessenger);
        if (messenger.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_MESSAGE, idMessenger), HttpStatus.NOT_FOUND);
        }
        if (messenger.get().getConversation().getId().equals(idConversation)
                && conversation.get().getIdSender().getId().equals(idUser)) {
            messengerService.delete(messenger.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }
}
