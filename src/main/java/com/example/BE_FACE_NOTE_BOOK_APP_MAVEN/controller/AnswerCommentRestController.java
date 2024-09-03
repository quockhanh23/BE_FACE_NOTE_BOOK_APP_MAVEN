package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Common;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.MessageResponse;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.AnswerCommentDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.AnswerComment;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Comment;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Notification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.AnswerCommentService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.CommentService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.NotificationService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/answerComments")
@Slf4j
public class AnswerCommentRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private AnswerCommentService answerCommentService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/allAnswerComment")
    public ResponseEntity<?> allAnswerComment() {
        List<AnswerComment> answerCommentList = answerCommentService.findAllByDeleteAtIsNull();
        if (CollectionUtils.isEmpty(answerCommentList)) {
            answerCommentList = new ArrayList<>();
        }
        return new ResponseEntity<>(answerCommentList, HttpStatus.OK);
    }

    @GetMapping("/allAnswerCommentByIdComment")
    public ResponseEntity<?> allAnswerCommentByIdComment(@RequestParam Long idComment) {
        List<AnswerComment> answerCommentList = answerCommentService.findAllByCommentIdAndDeleteAtIsNull(idComment);
        if (CollectionUtils.isEmpty(answerCommentList)) {
            answerCommentList = new ArrayList<>();
        }
        return new ResponseEntity<>(answerCommentList, HttpStatus.OK);
    }

    // Tạo mới AnswerComment
    @PostMapping("/createAnswerComment")
    public ResponseEntity<?> createAnswerComment(@RequestBody AnswerComment answerComment,
                                                 @RequestParam Long idUser,
                                                 @RequestParam Long idComment,
                                                 @SuppressWarnings("unused")
                                                 @RequestHeader("Authorization") String authorization) {
        if (StringUtils.isEmpty(answerComment.getContent().trim())) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField(Constants.DataField.CONTENT),
                    HttpStatus.BAD_REQUEST);
        }
        Common.handlerWordsLanguage(answerComment);
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<Comment> commentOptional = commentService.findById(idComment);
        if (commentOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_COMMENT, idComment), HttpStatus.NOT_FOUND);
        }
        answerCommentService.create(answerComment);
        answerComment.setComment(commentOptional.get());
        answerComment.setUser(userOptional.get());
        answerCommentService.save(answerComment);

        AnswerCommentDTO answerCommentDTO = modelMapper.map(answerComment, AnswerCommentDTO.class);
        answerCommentDTO.setCommentDTO(commentService.mapper(commentOptional.get()));
        answerCommentDTO.setUserDTO(userService.mapper(userService.checkUser(idUser)));
        if (!commentOptional.get().getUser().getId().equals(idUser)) {
            String title = Constants.Notification.TITLE_ANSWER_COMMENT;
            String type = Constants.Notification.TYPE_ANSWER_COMMENT;
            Notification notification = notificationService.
                    createDefault(commentOptional.get().getUser(), userOptional.get(), title, idComment, type);
            notificationService.save(notification);
        }
        return new ResponseEntity<>(answerCommentDTO, HttpStatus.OK);
    }

    // Xóa AnswerComment
    @DeleteMapping("/deleteAnswerComment")
    public ResponseEntity<?> deleteAnswerComment(@RequestParam Long idUser,
                                                 @RequestParam Long idComment,
                                                 @RequestParam Long idAnswerComment,
                                                 @SuppressWarnings("unused")
                                                 @RequestHeader("Authorization") String authorization) {
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<Comment> commentOptional = commentService.findById(idComment);
        if (commentOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_COMMENT, idComment), HttpStatus.NOT_FOUND);
        }
        Optional<AnswerComment> answerComment = answerCommentService.findById(idAnswerComment);
        if (answerComment.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_ANSWER_COMMENT, idAnswerComment), HttpStatus.NOT_FOUND);
        }
        if ((userOptional.get().getId().equals(answerComment.get().getUser().getId())) ||
                (userOptional.get().getId().equals(answerComment.get().getComment().getUser().getId()))) {
            answerComment.get().setDeleteAt(LocalDateTime.now());
            answerComment.get().setDelete(true);
            answerCommentService.save(answerComment.get());
            return new ResponseEntity<>(answerComment.get(), HttpStatus.OK);
        }
        if ((userOptional.get().getId().equals(answerComment.get().getComment().getPost().getUser().getId()))) {
            answerComment.get().setDeleteAt(LocalDateTime.now());
            answerComment.get().setDelete(true);
            answerCommentService.save(answerComment.get());
            return new ResponseEntity<>(answerComment.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }
}
