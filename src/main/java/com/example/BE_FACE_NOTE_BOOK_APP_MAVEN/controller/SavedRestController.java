package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.MessageResponse;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.GroupPost;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Post2;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Saved;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.SavedRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.GroupPostService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.PostService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/saves")
@Slf4j
@RequiredArgsConstructor
public class SavedRestController {

    private final SavedRepository savedRepository;

    private final GroupPostService groupPostService;

    private final UserService userService;

    private final PostService postService;

    // Danh sách đã lưu
    @GetMapping("/listSavedPost")
    public ResponseEntity<Object> listSavedPost(@RequestParam Long idUser) {
        List<Saved> savedList = savedRepository.findAllSavedPost(idUser);
        if (CollectionUtils.isEmpty(savedList)) {
            savedList = new ArrayList<>();
        }
        return new ResponseEntity<>(savedList, HttpStatus.OK);
    }

    // Lưu trữ bài viết
    @GetMapping("/savePost")
    public ResponseEntity<Object> savePost(@RequestParam Long idPost,
                                           @RequestParam Long idUser,
                                           @RequestParam String type) {
        User user = userService.checkExistUser(idUser);
        List<Saved> savedList = savedRepository.findAll();
        if (!CollectionUtils.isEmpty(savedList)) {
            for (int i = 0; i < savedList.size(); i++) {
                if (savedList.get(i).getIdUser().equals(user.getId())
                        && savedList.get(i).getIdPost().equals(idPost)) {
                    if (savedList.get(i).getStatus().equals(Constants.STATUS_SAVED)) {
                        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                                MessageResponse.TipMessage.SAVED),
                                HttpStatus.BAD_REQUEST);
                    }
                    if (savedList.get(i).getStatus().equals(Constants.STATUS_DELETE)) {
                        savedList.get(i).setStatus(Constants.STATUS_SAVED);
                        savedList.get(i).setSaveDate(new Date());
                        savedRepository.save(savedList.get(i));
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                }
            }
        }
        Saved saved = new Saved();
        if ("post".equalsIgnoreCase(type)) {
            Post2 post = postService.checkExistPost(idPost);
            saved.setType("Post");
            saved.setUserCreate(post.getUser().getFullName());
            saved.setImagePost(post.getImage());
            saved.setContent(post.getContent());
        }
        if ("groupPost".equalsIgnoreCase(type)) {
            Optional<GroupPost> groupPost = groupPostService.findById(idPost);
            if (groupPost.isEmpty()) {
                return new ResponseEntity<>(ResponseNotification.
                        responseMessage(Constants.IdCheck.ID_POST, idPost), HttpStatus.NOT_FOUND);
            }
            saved.setType("Group post");
            saved.setGroupName(groupPost.get().getTheGroup().getGroupName());
            saved.setUserCreate(groupPost.get().getCreateBy());
            saved.setImagePost(groupPost.get().getImage());
            saved.setContent(groupPost.get().getContent());
        }
        saved.setIdPost(idPost);
        saved.setSaveDate(new Date());
        saved.setStatus(Constants.STATUS_SAVED);
        saved.setIdUser(idUser);
        savedRepository.save(saved);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Xóa bài viết đã lưu trữ
    @GetMapping("/removeSavePost")
    public ResponseEntity<Object> removeSavePost(@RequestParam Long idPost, @RequestParam Long idSaved) {
        Post2 post = postService.checkExistPost(idPost);
        Optional<Saved> savedOptional = savedRepository.findById(idSaved);
        if (savedOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_SAVE, idSaved),
                    HttpStatus.NOT_FOUND);
        }
        if (savedOptional.get().getIdPost().equals(post.getId())) {
            savedOptional.get().setStatus(Constants.STATUS_DELETE);
            savedRepository.save(savedOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.ERROR), HttpStatus.BAD_REQUEST);
    }
}
