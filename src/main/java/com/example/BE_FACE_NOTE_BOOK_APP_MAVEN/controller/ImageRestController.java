package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.MessageResponse;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Image;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.ImageRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.ImageService;
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
@RequestMapping("/api/images")
@Slf4j
@RequiredArgsConstructor
public class ImageRestController {

    private final ImageService imageService;

    private final UserService userService;

    private final ImageRepository imageRepository;

    @GetMapping("/findAllImages")
    public ResponseEntity<Object> findAllImages(@RequestParam Long idUser,
                                                @RequestParam String type,
                                                @RequestParam Long idUserVisit,
                                                @RequestHeader("Authorization") String authorization) {
        boolean check;
        if (idUserVisit != null && "visit".equals(type)) {
            check = userService.errorToken(authorization, idUserVisit);
        } else {
            check = userService.errorToken(authorization, idUser);
        }
        if (!check) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        List<Image> imageList = imageService.findAllImageByIdUser(idUser);
        if (CollectionUtils.isEmpty(imageList)) {
            imageList = new ArrayList<>();
        }
        if ("visit".equals(type)) {
            imageList.removeIf(item -> item.getStatus().equals(Constants.STATUS_PRIVATE));
        }
        return new ResponseEntity<>(imageList, HttpStatus.OK);
    }

    @GetMapping("/findById")
    public ResponseEntity<Object> findById(@RequestParam Long idImage) {
        Optional<Image> imageOptional = imageService.findById(idImage);
        if (imageOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(imageOptional.get(), HttpStatus.OK);
    }

    // Thêm ảnh
    @PostMapping("/addPhoto")
    public ResponseEntity<Object> addPhoto(@RequestBody Image image,
                                           @RequestParam Long idUser,
                                           @SuppressWarnings("unused")
                                           @RequestHeader("Authorization") String authorization) {
        User user = userService.checkExistUser(idUser);
        Image imageDefault = imageService.createImageDefault(image.getLinkImage(), user);
        imageService.save(imageDefault);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @DeleteMapping("/actionPhoto")
    public ResponseEntity<Object> actionPhoto(@RequestParam Long idUser,
                                              @RequestParam Long idImage,
                                              @RequestParam String type,
                                              @SuppressWarnings("unused")
                                              @RequestHeader("Authorization") String authorization) {
        User user = userService.checkExistUser(idUser);
        Optional<Image> imageOptional = imageService.findById(idImage);
        if (imageOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_IMAGE, idImage),
                    HttpStatus.NOT_FOUND);
        }
        boolean isImageOfUser = imageOptional.get().getIdUser().equals(user.getId());
        if (!isImageOfUser) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                    HttpStatus.BAD_REQUEST);
        }
        switch (type) {
            case "Private" -> {
                imageOptional.get().setStatus(Constants.STATUS_PRIVATE);
                imageService.save(imageOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            case "Public" -> {
                imageOptional.get().setStatus(Constants.STATUS_PUBLIC);
                imageService.save(imageOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            case "Delete" -> {
                imageOptional.get().setStatus(Constants.STATUS_DELETE);
                imageOptional.get().setDeleteAt(new Date());
                imageService.save(imageOptional.get());
                if (user.getCover().equals(imageOptional.get().getLinkImage())) {
                    user.setCover(Constants.ImageDefault.DEFAULT_BACKGROUND_2);
                    userService.save(user);
                }
                if (user.getAvatar().equals(imageOptional.get().getLinkImage())) {
                    if (user.getGender().equals(Constants.GENDER_FEMALE)) {
                        user.setAvatar(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_FEMALE);
                    } else if (user.getGender().equals(Constants.GENDER_DEFAULT)) {
                        user.setAvatar(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_LGBT);
                    } else {
                        user.setAvatar(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_MALE);
                    }
                    userService.save(user);
                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
            case "Restore" -> {
                imageOptional.get().setStatus(Constants.STATUS_PUBLIC);
                imageOptional.get().setDeleteAt(null);
                imageService.save(imageOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            case "DeleteDB" -> {
                imageService.delete(imageOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
    }

    @GetMapping("/getAllImageDeleted")
    public ResponseEntity<Object> getAllImageDeleted(@RequestParam Long idUser,
                                                     @SuppressWarnings("unused")
                                                     @RequestHeader("Authorization") String authorization) {
        userService.checkExistUser(idUser);
        List<Image> imageListDeleted = imageService.findAllImageDeletedByUserId(idUser);
        if (CollectionUtils.isEmpty(imageListDeleted)) {
            imageListDeleted = new ArrayList<>();
        }
        return new ResponseEntity<>(imageListDeleted, HttpStatus.OK);
    }

    // Xóa tất cả ảnh trong thùng rác
    @DeleteMapping("/actionAllImage")
    public ResponseEntity<Object> deleteAllImage(@RequestParam Long idUser,
                                                 @RequestParam String type,
                                                 @SuppressWarnings("unused")
                                                 @RequestHeader("Authorization") String authorization) {
        userService.checkExistUser(idUser);
        List<Image> imageListDeleted = imageService.findAllImageDeletedByUserId(idUser);
        if (!CollectionUtils.isEmpty(imageListDeleted)) {
            if (Constants.DELETE_ALL.equalsIgnoreCase(type)) {
                imageRepository.deleteAllInBatch(imageListDeleted);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            if (Constants.RESTORE_ALL.equalsIgnoreCase(type)) {
                imageListDeleted.forEach(image -> {
                    image.setStatus(Constants.STATUS_PUBLIC);
                    image.setDeleteAt(null);
                });
                imageRepository.saveAll(imageListDeleted);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }
}
