package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Common;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.MessageResponse;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Regex;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.CommentDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.*;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.*;
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
import java.util.stream.Collectors;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/comments")
@Slf4j
public class CommentRestController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private LikeCommentService likeCommentService;
    @Autowired
    private DisLikeCommentService disLikeCommentService;
    @Autowired
    private AnswerCommentService answerCommentService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/allComment")
    public ResponseEntity<List<Comment>> allComment() {
        List<Comment> list = commentService.getCommentTrue();
        if (CollectionUtils.isEmpty(list)) list = new ArrayList<>();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/allCommentUpdated")
    public ResponseEntity<List<Comment>> allCommentUpdated() {
        List<Comment> list = commentService.getCommentTrue();
        if (!CollectionUtils.isEmpty(list)) {
            List<Long> listIdComment = list.stream().map(Comment::getId).distinct().collect(Collectors.toList());
            List<LikeComment> likeCommentList = likeCommentService.findAllByCommentIdIn(listIdComment);
            List<DisLikeComment> disLikeCommentList = disLikeCommentService.findAllByCommentIdIn(listIdComment);
            for (Long idComment : listIdComment) {
                List<LikeComment> likeComments = likeCommentList.stream().
                        filter(item -> item.getComment().getId().equals(idComment)).toList();
                list.stream().filter(item -> item.getId().equals(idComment)).findFirst().ifPresent(comment -> {
                    comment.setNumberLike((long) likeComments.size());
                });
                List<DisLikeComment> disLikeComments = disLikeCommentList.stream().
                        filter(item -> item.getComment().getId().equals(idComment)).toList();
                list.stream().filter(item -> item.getId().equals(idComment)).findFirst().ifPresent(comment -> {
                    comment.setNumberDisLike((long) disLikeComments.size());
                });
            }
            commentService.saveAll(list);
        } else {
            list = new ArrayList<>();
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // Tạo mới comment
    @PostMapping("/createComment")
    public ResponseEntity<?> createComment(@RequestBody Comment comment,
                                           @RequestParam Long idUser,
                                           @RequestParam Long idPost) {
        if (StringUtils.isEmpty(comment.getContent().trim())
                || !Common.checkRegex(comment.getContent(), Regex.CHECK_LENGTH_COMMENT)) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField(Constants.DataField.CONTENT),
                    HttpStatus.BAD_REQUEST);
        }
        Common.handlerWordsLanguage(comment);
        Optional<Post2> post2Optional = postService.findById(idPost);
        if (post2Optional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_POST, idPost), HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        commentService.create(comment);
        comment.setPost(post2Optional.get());
        comment.setUser(userOptional.get());
        commentService.save(comment);
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        commentDTO.setUserDTO(userService.mapper(userService.checkUser(idUser)));
        commentDTO.setPostDTO(postService.mapper(post2Optional.get()));
        if (!post2Optional.get().getUser().getId().equals(idUser)) {
            String title = Constants.Notification.TITLE_COMMENT;
            String type = Constants.Notification.TYPE_COMMENT;
            Notification notification = notificationService.
                    createDefault(post2Optional.get().getUser(), userOptional.get(), title, idPost, type);
            notificationService.save(notification);
        }
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    // Xóa comment và xóa các answer comment
    @DeleteMapping("/deleteComment")
    public ResponseEntity<?> deleteComment(@RequestParam Long idUser,
                                           @RequestParam Long idComment,
                                           @RequestParam Long idPost) {
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<Comment> commentOptional = commentService.findById(idComment);
        if (commentOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification
                    .responseMessage(Constants.IdCheck.ID_COMMENT, idComment), HttpStatus.NOT_FOUND);
        }
        Optional<Post2> post2Optional = postService.findById(idPost);
        if (post2Optional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        if ((userOptional.get().getId().equals(commentOptional.get().getUser().getId())) ||
                (userOptional.get().getId().equals(commentOptional.get().getPost().getUser().getId()))) {
            commentOptional.get().setDeleteAt(LocalDateTime.now());
            commentOptional.get().setDelete(true);
            commentService.save(commentOptional.get());
            List<AnswerComment> answerCommentList = answerCommentService.
                    findAllByCommentIdAndDeleteAtIsNull(commentOptional.get().getId());
            for (AnswerComment answerComment : answerCommentList) {
                answerComment.setDeleteAt(LocalDateTime.now());
                answerComment.setDelete(true);
            }
            answerCommentService.saveAll(answerCommentList);
            return new ResponseEntity<>(commentOptional.get().isDelete(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    // Xem tất cả comment của 1 bài viết theo người comment
    @GetMapping("/getAllCommentByIdUser")
    public ResponseEntity<List<Comment>> getAllCommentByIdUser(@RequestParam Long idUser) {
        List<Comment> commentList = commentService.findAllByUserId(idUser);
        if (CollectionUtils.isEmpty(commentList)) commentList = new ArrayList<>();
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    // Xem tất cả comment của 1 bài viết theo bài viết
    @GetMapping("/getAllCommentByIdPost")
    public ResponseEntity<List<Comment>> getAllCommentByIdPost(@RequestParam Long idPost) {
        List<Comment> commentList = commentService.getCommentByIdPost(idPost);
        if (CollectionUtils.isEmpty(commentList)) commentList = new ArrayList<>();
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }
}
