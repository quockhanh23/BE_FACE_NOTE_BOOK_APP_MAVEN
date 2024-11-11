package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Common;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.MessageResponse;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Regex;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.PostDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.UserDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.*;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.*;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/posts")
@Slf4j
public class PostRestController {

    private final PostService postService;

    private final UserService userService;

    private final LikePostService likePostService;

    private final DisLikePostService disLikePostService;

    private final IconHeartService iconHeartService;

    private final ModelMapper modelMapper;

    private final ImageService imageService;

    private final HidePostRepository hidePostRepository;

    private final CommentRepository commentRepository;

    private final LikePostRepository likePostRepository;

    private final DisLikePostRepository disLikePostRepository;

    private final IconHeartRepository iconHeartRepository;

    private final PostRepository postRepository;

    @Autowired
    public PostRestController(PostService postService,
                              UserService userService,
                              LikePostService likePostService,
                              DisLikePostService disLikePostService,
                              IconHeartService iconHeartService,
                              ModelMapper modelMapper,
                              ImageService imageService,
                              HidePostRepository hidePostRepository,
                              CommentRepository commentRepository,
                              LikePostRepository likePostRepository,
                              DisLikePostRepository disLikePostRepository,
                              IconHeartRepository iconHeartRepository,
                              PostRepository postRepository) {
        this.postService = postService;
        this.userService = userService;
        this.likePostService = likePostService;
        this.disLikePostService = disLikePostService;
        this.iconHeartService = iconHeartService;
        this.modelMapper = modelMapper;
        this.imageService = imageService;
        this.hidePostRepository = hidePostRepository;
        this.commentRepository = commentRepository;
        this.likePostRepository = likePostRepository;
        this.disLikePostRepository = disLikePostRepository;
        this.iconHeartRepository = iconHeartRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/allPostPublic")
    public ResponseEntity<Object> allPostPublic(@RequestParam Long idUser,
                                                @RequestParam String type,
                                                @RequestParam(required = false) Long idUserVisit,
                                                @RequestHeader("Authorization") String authorization) {
        try {
            boolean check;
            if (idUserVisit != null) {
                check = userService.errorToken(authorization, idUserVisit);
            } else {
                check = userService.errorToken(authorization, idUser);
            }
            if (!check) {
                return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                        Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                        HttpStatus.UNAUTHORIZED);
            }
            List<Post2> post2List = postService.allPost(idUser);
            if (!CollectionUtils.isEmpty(post2List)) {
                if (type.equals("detailUser")) {
                    post2List = postService.findAllPostByUser(idUser);
                    List<PostDTO> postDTOList = postService.filterListPost(post2List);
                    return new ResponseEntity<>(postDTOList, HttpStatus.OK);
                }
                if (type.equals("getAll")) {
                    List<PostDTO> postDTOList = postService.filterListPost(post2List);
                    return new ResponseEntity<>(postDTOList, HttpStatus.OK);
                }
                if (type.equals("getAllByUser")) {
                    List<PostDTO> postDTOList = postService.filterListPost(post2List);
                    List<HidePost> hidePosts = hidePostRepository.findAllByIdUser(idUser);
                    if (!CollectionUtils.isEmpty(hidePosts)) {
                        List<Long> listIdPost = hidePosts.stream().map(HidePost::getIdPost).toList();
                        for (Long id : listIdPost) {
                            postDTOList.stream().filter(item -> item.getId().equals(id)).findFirst()
                                    .ifPresent(post2 -> post2.setContent(null));
                        }
                    }
                    return new ResponseEntity<>(postDTOList, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @DeleteMapping("/hidePost")
    public ResponseEntity<Object> hidePost(@RequestParam Long idUser, @RequestParam Long idPost,
                                           @RequestParam String type,
                                           @SuppressWarnings("unused")
                                           @RequestHeader("Authorization") String authorization) {
        List<HidePost> list = hidePostRepository.findAllByIdUser(idUser);
        if (Constants.HIDE.equalsIgnoreCase(type)) {
            if (!CollectionUtils.isEmpty(list)) {
                for (HidePost post : list) {
                    if (post.getIdPost().equals(idPost) && post.getIdUser().equals(idUser)) {
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                }
            }
            HidePost hidePost = new HidePost();
            hidePost.setCreateAt(new Date());
            hidePost.setIdUser(idUser);
            hidePost.setIdPost(idPost);
            hidePostRepository.save(hidePost);
        }
        if (Constants.UN_HIDE.equalsIgnoreCase(type) && !CollectionUtils.isEmpty(list)) {
            for (HidePost post : list) {
                if (post.getIdPost().equals(idPost) && post.getIdUser().equals(idUser)) {
                    hidePostRepository.delete(post);
                    break;
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/createPost")
    @Transactional()
    public ResponseEntity<Object> createPost(@RequestBody Post2 post,
                                             @RequestParam Long idUser,
                                             @SuppressWarnings("unused")
                                             @RequestHeader("Authorization") String authorization) {
        if (((StringUtils.isNotEmpty(post.getContent()) && post.getContent().trim().equals(Constants.BLANK))
                || (post.getContent() == null && post.getImage() == null))
                || !Common.checkRegex(post.getContent(), Regex.CHECK_LENGTH_POST)) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField(Constants.DataField.CONTENT),
                    HttpStatus.BAD_REQUEST);
        }
        Common.handlerWordsLanguage(post);
        if (StringUtils.isNotEmpty(post.getImage())) {
            Image image = imageService.createImageDefault(post.getImage(), post.getUser());
            imageService.save(image);
        }
        User user = userService.checkExistUser(idUser);
        postService.create(post);
        post.setUser(user);
        postService.save(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updatePost")
    @Transactional()
    public ResponseEntity<Object> updatePost(@RequestParam Long idPost, @RequestParam Long idUser,
                                             @RequestBody Post2 post,
                                             @SuppressWarnings("unused")
                                             @RequestHeader("Authorization") String authorization) {
        Post2 postOptional = postService.checkExistPost(idPost);
        User user = userService.checkExistUser(idUser);
        Common.handlerWordsLanguage(post);
        if (postOptional.getUser().getId().equals(user.getId())) {
            postOptional.setEditAt(new Date());
            if (StringUtils.isNotEmpty(post.getContent()) && post.getContent().trim().equals(Constants.BLANK)) {
                postOptional.setContent(post.getContent());
            }
            postService.save(postOptional);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/updateReflectPost")
    public ResponseEntity<Object> updateReflectPost(@RequestParam Long idPost,
                                                    @RequestParam String type) {
        Post2 post = postService.checkExistPost(idPost);
        List<LikePost> likePost = new ArrayList<>();
        List<DisLikePost> disLikePosts = new ArrayList<>();
        List<IconHeart> iconHearts = new ArrayList<>();
        switch (type) {
            case "like" -> likePost = likePostService.findAllLikeByPostId(idPost);
            case "disLike" -> disLikePosts = disLikePostService.findAllDisLikeByPostId(idPost);
            case "heart" -> iconHearts = iconHeartService.findAllHeartByPostId(idPost);
            default -> {
                likePost = likePostService.findAllLikeByPostId(idPost);
                disLikePosts = disLikePostService.findAllDisLikeByPostId(idPost);
                iconHearts = iconHeartService.findAllHeartByPostId(idPost);
            }
        }
        post.setNumberLike((long) likePost.size());
        post.setNumberDisLike((long) disLikePosts.size());
        post.setIconHeart((long) iconHearts.size());
        post.setIconHeart((long) iconHearts.size());
        postService.save(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/changeStatusPost")
    public ResponseEntity<Object> changeStatusPost(@RequestParam Long idPost,
                                                   @RequestParam Long idUser,
                                                   @RequestParam String type,
                                                   @SuppressWarnings("unused")
                                                   @RequestHeader("Authorization") String authorization) {
        Post2 post = postService.checkExistPost(idPost);
        userService.checkExistUser(idUser);
        switch (type) {
            case "Public" -> post.setStatus(Constants.STATUS_PUBLIC);
            case "Private" -> post.setStatus(Constants.STATUS_PRIVATE);
            case "Delete" -> {
                post.setStatus(Constants.STATUS_DELETE);
                post.setDelete(true);
            }
            default -> {
                return new ResponseEntity<>(MessageResponse.IN_VALID, HttpStatus.NOT_FOUND);
            }
        }
        postService.save(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/actionAllPost")
    public ResponseEntity<Object> actionAllPost(@RequestParam List<Long> listIdPost,
                                                @RequestParam Long idUser,
                                                @RequestParam String type,
                                                @SuppressWarnings("unused")
                                                @RequestHeader("Authorization") String authorization) {
        userService.checkExistUser(idUser);
        List<Post2> post2List = postRepository.findAllById(listIdPost);
        // Xóa tất cả bài viết
        if (Constants.DELETE_ALL.equalsIgnoreCase(type)) {
            List<Long> listIdPosts = post2List.stream().map(Post2::getId).collect(Collectors.toList());
            List<Comment> comments = commentRepository.findAllByPostIdInAndDeleteAtIsNull(listIdPosts);
            List<LikePost> likePosts = likePostService.findAllByPostIdIn(listIdPosts);
            List<DisLikePost> disLikePosts = disLikePostService.findAllByPostIdIn(listIdPosts);
            List<IconHeart> iconHearts = iconHeartService.findAllByPostIdIn(listIdPosts);
            postService.deleteRelateOfComment(comments);
            commentRepository.deleteAllInBatch(comments);
            likePostRepository.deleteAllInBatch(likePosts);
            disLikePostRepository.deleteAllInBatch(disLikePosts);
            iconHeartRepository.deleteAllInBatch(iconHearts);
            postRepository.deleteAllInBatch(post2List);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        // Khôi phục tất cả
        if (Constants.RESTORE_ALL.equalsIgnoreCase(type)) {
            post2List.forEach(post2 -> {
                post2.setStatus(Constants.STATUS_PUBLIC);
                post2.setDelete(false);
            });
            postRepository.saveAll(post2List);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping("findOnePostById")
    public ResponseEntity<Object> findOnePostById(@RequestParam Long idPost,
                                                  @RequestHeader("Authorization") String authorization) {
        Post2 post = postService.checkExistPost(idPost);
        if (!userService.errorToken(authorization, post.getUser().getId())) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        UserDTO userDTO = modelMapper.map(post.getUser(), UserDTO.class);
        postDTO.setUserDTO(userDTO);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @GetMapping("/allPostInTrash")
    public ResponseEntity<Object> allPostInTrash(@RequestParam Long idUser,
                                                 @RequestHeader("Authorization") String authorization) {
        if (!userService.errorToken(authorization, idUser)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN +
                    StringUtils.SPACE + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        List<Post2> post2List = postService.findAllByUserIdAndDeleteTrue(idUser);
        List<PostDTO> postDTOList = postService.filterListPost(post2List);
        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }
}
