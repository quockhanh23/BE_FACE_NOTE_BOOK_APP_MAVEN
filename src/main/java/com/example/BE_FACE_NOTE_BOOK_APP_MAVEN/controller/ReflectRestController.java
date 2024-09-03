package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.*;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/refs")
@Slf4j
public class ReflectRestController {
    @Autowired
    private LikePostService likePostService;
    @Autowired
    private DisLikePostService disLikePostService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private LikeCommentService likeCommentService;
    @Autowired
    private DisLikeCommentService disLikeCommentService;
    @Autowired
    private IconHeartService iconHeartService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NotificationService notificationService;

    // Xem like của post
    @GetMapping("/getAllLike")
    public ResponseEntity<?> getAllLike(@RequestParam Long idPost) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        List<LikePost> likePosts = likePostService.findAllLikeByPostId(idPost);
        if (CollectionUtils.isEmpty(likePosts)) {
            likePosts = new ArrayList<>();
        }
        return new ResponseEntity<>(likePosts, HttpStatus.OK);
    }

    @GetMapping("/getAllHeart")
    public ResponseEntity<?> getAllHeart(@RequestParam Long idPost) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        List<IconHeart> iconHearts = iconHeartService.findAllHeartByPostId(idPost);
        if (CollectionUtils.isEmpty(iconHearts)) {
            iconHearts = new ArrayList<>();
        }
        return new ResponseEntity<>(iconHearts, HttpStatus.OK);
    }

    // Xem dislike của post
    @GetMapping("/getAllDisLike")
    public ResponseEntity<?> getAllDisLike(@RequestParam Long idPost) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        List<DisLikePost> disLikePosts = disLikePostService.findAllDisLikeByPostId(idPost);
        if (CollectionUtils.isEmpty(disLikePosts)) {
            disLikePosts = new ArrayList<>();
        }
        return new ResponseEntity<>(disLikePosts, HttpStatus.OK);
    }

    @DeleteMapping("/actionReflectPost")
    public ResponseEntity<?> actionReflectPost(@RequestParam Long idPost,
                                               @RequestParam Long idUser,
                                               @RequestParam String type) {
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<Post2> postOptional = postService.findById(idPost);
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        String title = "";
        if ("like".equalsIgnoreCase(type)) {
            title = Constants.Notification.TITLE_LIKE_POST;
            List<LikePost> likePostIterable = likePostService.findLike(idPost, idUser);
            if (likePostIterable.size() == 1) {
                likePostService.delete(likePostIterable.get(0));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            LikePost likePost = new LikePost();
            likePost.setUserLike(userOptional.get());
            likePost.setCreateAt(LocalDateTime.now());
            likePost.setPost(postOptional.get());
            likePostService.save(likePost);
        }
        if ("heart".equalsIgnoreCase(type)) {
            title = Constants.Notification.TITLE_HEART_POST;
            List<IconHeart> iconHearts = iconHeartService.findHeart(idPost, idUser);
            if (iconHearts.size() == 1) {
                iconHeartService.delete(iconHearts.get(0));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            IconHeart iconHeart = new IconHeart();
            iconHeart.setUser(userOptional.get());
            iconHeart.setCreateAt(new Date());
            iconHeart.setPost(postOptional.get());
            iconHeartService.save(iconHeart);
        }
        if ("disLike".equalsIgnoreCase(type)) {
            title = Constants.Notification.TITLE_DISLIKE_POST;
            List<DisLikePost> disLikePosts = disLikePostService.findDisLike(idPost, idUser);
            if (disLikePosts.size() == 1) {
                disLikePosts.get(0).setUserDisLike(null);
                disLikePostService.save(disLikePosts.get(0));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            DisLikePost disLikePost = new DisLikePost();
            disLikePost.setUserDisLike(userOptional.get());
            disLikePost.setCreateAt(new Date());
            disLikePost.setPost(postOptional.get());
            disLikePostService.save(disLikePost);
        }
        if (!postOptional.get().getUser().getId().equals(idUser)) {
            String typeNotification = Constants.Notification.TYPE_POST;
            Notification notification = notificationService.
                    createDefault(postOptional.get().getUser(), userOptional.get(), title, idPost, typeNotification);
            notificationService.save(notification);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/actionReflectComment")
    public ResponseEntity<?> actionReflectComment(@RequestParam Long idComment,
                                                  @RequestParam Long idUser,
                                                  @RequestParam String type) {
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<Comment> commentOptional = commentService.findById(idComment);
        if (commentOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_COMMENT, idComment),
                    HttpStatus.NOT_FOUND);
        }
        String title = "";
        if ("like".equalsIgnoreCase(type)) {
            title = Constants.Notification.TITLE_LIKE_COMMENT;
            List<LikeComment> likeComments = likeCommentService.findLikeComment(idComment, idUser);
            if (likeComments.size() == 1) {
                likeCommentService.delete(likeComments.get(0));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            LikeComment likeComment = new LikeComment();
            likeComment.setUserLike(userOptional.get());
            likeComment.setCreateAt(new Date());
            likeComment.setComment(commentOptional.get());
            likeCommentService.save(likeComment);
        }
        if ("disLike".equalsIgnoreCase(type)) {
            title = Constants.Notification.TITLE_DISLIKE_COMMENT;
            List<DisLikeComment> disLikeComments = disLikeCommentService.findDisLikeComment(idComment, idUser);
            if (disLikeComments.size() == 1) {
                disLikeCommentService.delete(disLikeComments.get(0));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            DisLikeComment disLikeComment = new DisLikeComment();
            disLikeComment.setUserDisLike(userOptional.get());
            disLikeComment.setCreateAt(new Date());
            disLikeComment.setComment(commentOptional.get());
            disLikeCommentService.save(disLikeComment);
        }
        if (!commentOptional.get().getUser().getId().equals(idUser)) {
            String typeNotification = Constants.Notification.TYPE_COMMENT;
            Notification notification = notificationService.
                    createDefault(commentOptional.get().getUser(), userOptional.get(), title, idComment, typeNotification);
            notificationService.save(notification);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
