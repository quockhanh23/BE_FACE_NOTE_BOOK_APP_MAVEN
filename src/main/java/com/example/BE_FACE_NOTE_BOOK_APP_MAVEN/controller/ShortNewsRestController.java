package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Common;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.MessageResponse;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.ShortNewsDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.UserDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.ShortNews;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.ShortNewsRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.ShortNewsService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/news")
@Slf4j
public class ShortNewsRestController {

    private final ShortNewsService shortNewsService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final ShortNewsRepository shortNewsRepository;

    @Autowired
    public ShortNewsRestController(ShortNewsService shortNewsService,
                                   UserService userService,
                                   ModelMapper modelMapper,
                                   ShortNewsRepository shortNewsRepository) {
        this.shortNewsService = shortNewsService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.shortNewsRepository = shortNewsRepository;
    }

    // 4 tin mới nhất
    @GetMapping("/shortNewsLimit")
    public ResponseEntity<List<ShortNews>> shortNewsLimit() {
        List<ShortNews> shortNews = shortNewsService.findAllShortNews();
        if (CollectionUtils.isEmpty(shortNews)) {
            shortNews = new ArrayList<>();
        }
        return new ResponseEntity<>(shortNews, HttpStatus.OK);
    }

    // Tin của tôi
    @GetMapping("/myShortNews")
    public ResponseEntity<Object> myShortNews(@RequestParam Long idUser) {
        userService.checkExistUser(idUser);
        List<ShortNews> shortNews = shortNewsService.myShortNew(idUser);
        if (CollectionUtils.isEmpty(shortNews)) {
            shortNews = new ArrayList<>();
        }
        return new ResponseEntity<>(shortNews, HttpStatus.OK);
    }

    // Tất cả tin công khai
    @GetMapping("/allShortNewPublic")
    public ResponseEntity<Object> allShortNewPublic() {
        List<ShortNews> shortNews = shortNewsService.findAllShortNewsPublic();
        List<ShortNewsDTO> shortNewsDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shortNews)) {
            for (ShortNews news : shortNews) {
                UserDTO userDTO = modelMapper.map(news.getUser(), UserDTO.class);
                ShortNewsDTO shortNewsDTO = modelMapper.map(news, ShortNewsDTO.class);
                shortNewsDTO.setUserDTO(userDTO);
                shortNewsDTOList.add(shortNewsDTO);
            }
        }
        return new ResponseEntity<>(shortNewsDTOList, HttpStatus.OK);
    }

    // Kiểm tra hạn sử dụng
    @GetMapping("/checkExpiryDate")
    public ResponseEntity<Void> checkExpiryDate() {
        shortNewsService.checkExpiryDate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Tạo tin
    @PostMapping("/createShortNews")
    public ResponseEntity<Object> createShortNews(@RequestBody ShortNews shortNews,
                                             @RequestParam Long idUser) {
        if (StringUtils.isEmpty(shortNews.getContent())) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField(Constants.DataField.CONTENT),
                    HttpStatus.BAD_REQUEST);
        }
        Common.handlerWordsLanguage(shortNews);
        User user = userService.checkExistUser(idUser);
        shortNewsService.createShortNews(shortNews, user);
        shortNewsService.save(shortNews);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Chuyển vào thùng rác, Xóa tin trong database
    @DeleteMapping("/deleteShortNews")
    public ResponseEntity<Object> deleteShortNews(@RequestParam Long idSortNew,
                                             @RequestParam Long idUser,
                                             @RequestParam String type) {
        User user = userService.checkExistUser(idUser);
        Optional<ShortNews> shortNewsOptional = shortNewsService.findById(idSortNew);
        if (shortNewsOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_SORT_NEW, idSortNew), HttpStatus.NOT_FOUND);
        }
        if ("trash".equals(type)) {
            if (user.getId().equals(shortNewsOptional.get().getUser().getId())) {
                shortNewsOptional.get().setDelete(true);
                shortNewsOptional.get().setUpdatedAt(new Date());
                shortNewsService.save(shortNewsOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        if ("delete".equals(type)) {
            if (user.getId().equals(shortNewsOptional.get().getUser().getId())) {
                shortNewsOptional.get().setUpdatedAt(new Date());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    // Xóa tất cả, khôi phục tất cả
    @DeleteMapping("/actionShortNews")
    public ResponseEntity<Object> actionShortNews(@RequestParam List<Long> listIdSortNew,
                                             @RequestParam Long idUser,
                                             @RequestParam String type,
                                             @RequestHeader("Authorization") String authorization) {
        userService.checkExistUser(idUser);
        boolean check = userService.errorToken(authorization, idUser);
        if (!check) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN +
                    StringUtils.SPACE + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        List<ShortNews> shortNews = shortNewsRepository.findAllById(listIdSortNew);
        if (Constants.DELETE_ALL.equalsIgnoreCase(type)) {
            shortNewsRepository.deleteAllInBatch(shortNews);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if (Constants.RESTORE_ALL.equalsIgnoreCase(type)) {
            shortNews.forEach(shortNews1 -> shortNews1.setDelete(false));
            shortNewsRepository.saveAll(shortNews);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    // Các tin đã xóa
    @GetMapping("/shortNewsInTrash")
    public ResponseEntity<Object> shortNewsInTrash(@RequestParam Long idUser) {
        userService.checkExistUser(idUser);
        List<ShortNews> shortNews = shortNewsService.getListShortNewInTrash(idUser);
        if (CollectionUtils.isEmpty(shortNews)) {
            shortNews = new ArrayList<>();
        }
        return new ResponseEntity<>(shortNews, HttpStatus.OK);
    }
}
