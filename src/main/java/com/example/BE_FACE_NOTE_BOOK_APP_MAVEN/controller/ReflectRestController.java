package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.*;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@RequiredArgsConstructor
public class ReflectRestController {

    private final LikePostService likePostService;

    private final DisLikePostService disLikePostService;

    private final UserService userService;

    private final PostService postService;

    private final LikeCommentService likeCommentService;

    private final DisLikeCommentService disLikeCommentService;

    private final IconHeartService iconHeartService;

    private final CommentService commentService;

    private final NotificationService notificationService;

    @GetMapping("/getAllLike")
    public ResponseEntity<Object> getAllLike(@RequestParam Long idPost) {
        postService.checkExistPost(idPost);
        List<LikePost> likePosts = likePostService.findAllLikeByPostId(idPost);
        if (CollectionUtils.isEmpty(likePosts)) {
            likePosts = new ArrayList<>();
        }
        return new ResponseEntity<>(likePosts, HttpStatus.OK);
    }

    @GetMapping("/getAllHeart")
    public ResponseEntity<Object> getAllHeart(@RequestParam Long idPost) {
        postService.checkExistPost(idPost);
        List<IconHeart> iconHearts = iconHeartService.findAllHeartByPostId(idPost);
        if (CollectionUtils.isEmpty(iconHearts)) {
            iconHearts = new ArrayList<>();
        }
        return new ResponseEntity<>(iconHearts, HttpStatus.OK);
    }

    @GetMapping("/getAllDisLike")
    public ResponseEntity<Object> getAllDisLike(@RequestParam Long idPost) {
        postService.checkExistPost(idPost);
        List<DisLikePost> disLikePosts = disLikePostService.findAllDisLikeByPostId(idPost);
        if (CollectionUtils.isEmpty(disLikePosts)) {
            disLikePosts = new ArrayList<>();
        }
        return new ResponseEntity<>(disLikePosts, HttpStatus.OK);
    }

    @DeleteMapping("/actionReflectPost")
    public ResponseEntity<Object> actionReflectPost(@RequestParam Long idPost,
                                                    @RequestParam Long idUser,
                                                    @RequestParam String type) {
        User user = userService.checkExistUser(idUser);
        Post2 post = postService.checkExistPost(idPost);
        String title = StringUtils.EMPTY;
        if ("like".equalsIgnoreCase(type)) {
            title = Constants.Notification.TITLE_LIKE_POST;
            List<LikePost> likePostIterable = likePostService.findLike(idPost, idUser);
            if (likePostIterable.size() == 1) {
                likePostService.delete(likePostIterable.get(0));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            LikePost likePost = new LikePost();
            likePost.setUserLike(user);
            likePost.setCreateAt(LocalDateTime.now());
            likePost.setPost(post);
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
            iconHeart.setUser(user);
            iconHeart.setCreateAt(new Date());
            iconHeart.setPost(post);
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
            disLikePost.setUserDisLike(user);
            disLikePost.setCreateAt(new Date());
            disLikePost.setPost(post);
            disLikePostService.save(disLikePost);
        }
        if (!post.getUser().getId().equals(idUser)) {
            String typeNotification = Constants.Notification.TYPE_POST;
            Notification notification = notificationService.
                    createDefault(post.getUser(), user, title, idPost, typeNotification);
            notificationService.save(notification);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/actionReflectComment")
    public ResponseEntity<Object> actionReflectComment(@RequestParam Long idComment,
                                                       @RequestParam Long idUser,
                                                       @RequestParam String type) {
        User user = userService.checkExistUser(idUser);
        Optional<Comment> commentOptional = commentService.findById(idComment);
        if (commentOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_COMMENT, idComment),
                    HttpStatus.NOT_FOUND);
        }
        String title = StringUtils.EMPTY;
        if ("like".equalsIgnoreCase(type)) {
            title = Constants.Notification.TITLE_LIKE_COMMENT;
            List<LikeComment> likeComments = likeCommentService.findLikeComment(idComment, idUser);
            if (likeComments.size() == 1) {
                likeCommentService.delete(likeComments.get(0));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            LikeComment likeComment = new LikeComment();
            likeComment.setUserLike(user);
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
            disLikeComment.setUserDisLike(user);
            disLikeComment.setCreateAt(new Date());
            disLikeComment.setComment(commentOptional.get());
            disLikeCommentService.save(disLikeComment);
        }
        if (!commentOptional.get().getUser().getId().equals(idUser)) {
            String typeNotification = Constants.Notification.TYPE_COMMENT;
            Notification notification = notificationService.
                    createDefault(commentOptional.get().getUser(), user, title, idComment, typeNotification);
            notificationService.save(notification);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
