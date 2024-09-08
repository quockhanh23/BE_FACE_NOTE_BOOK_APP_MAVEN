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

import java.time.*;
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

    // Lưu ngày mới
    @GetMapping("/newDay")
    public ResponseEntity<Iterable<ShortNews>> newDay() {
        Iterable<ShortNews> shortNews = shortNewsService.findAll();
        List<ShortNews> shortNewsList;
        shortNewsList = (List<ShortNews>) shortNews;
        for (ShortNews news : shortNewsList) {
            news.setToDay(new Date());
        }
        shortNewsService.saveAll(shortNewsList);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<?> myShortNews(@RequestParam Long idUser) {
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        List<ShortNews> shortNews = shortNewsService.myShortNew(idUser);
        if (CollectionUtils.isEmpty(shortNews)) {
            shortNews = new ArrayList<>();
        }
        return new ResponseEntity<>(shortNews, HttpStatus.OK);
    }

    // Tất cả tin công khai
    @GetMapping("/allShortNewPublic")
    public ResponseEntity<?> allShortNewPublic() {
        List<ShortNews> shortNews = shortNewsService.findAllShortNewsPublic();
        List<ShortNewsDTO> shortNewsDTOList = new ArrayList<>();
        if (shortNews != null) {
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
    @GetMapping("/allShortNews")
    public ResponseEntity<?> allShortNews() {
        Iterable<ShortNews> shortNews = shortNewsService.findAll();
        List<ShortNews> shortNewsList;
        shortNewsList = (List<ShortNews>) shortNews;
        if (!CollectionUtils.isEmpty(shortNewsList)) {
            for (ShortNews item : shortNewsList) {

                int today = Integer.parseInt(item.getToDay().toString().substring(8, 10));
                int createDay = Integer.parseInt(item.getCreateAt().toString().substring(8, 10));

                int monthToday = Integer.parseInt(item.getToDay().toString().substring(5, 7));
                int monthCreate = Integer.parseInt(item.getCreateAt().toString().substring(5, 7));

                int yearToday = Integer.parseInt(item.getToDay().toString().substring(0, 4));
                int yearCreate = Integer.parseInt(item.getCreateAt().toString().substring(0, 4));

                int totalDay = 0;

                Instant instant = Instant.ofEpochMilli(item.getCreateAt().getTime());
                LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                Instant instant1 = Instant.ofEpochMilli(item.getToDay().getTime());
                LocalDateTime localDateTime1 = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault());

                LocalDate localDate = LocalDate.from(localDateTime);
                LocalDate localDate1 = LocalDate.from(localDateTime1);

                Period period = Period.between(localDate, localDate1);

                int getDay = period.getDays();
                int getMonth = period.getMonths();
                int getYear = period.getYears();

                if (getMonth > 0) {
                    int[] getMonthArr = new int[getMonth];
                    if (getMonth == 1) {
                        getMonthArr[0] = 1;
                    }
                    if (getMonth >= 2) {
                        int value = 1;
                        for (int j = 0; j < getMonth; j++) {
                            getMonthArr[j] = value;
                            value++;
                        }
                    }

                    int[] month31Day = {1, 3, 5, 7, 8, 10, 12};
                    int[] month30Day = {4, 6, 9, 11};
                    int month28DayOr29Day = 2;

                    for (int k = 0; k < getMonthArr.length; k++) {
                        for (int j = 0; j < month31Day.length; j++) {
                            if (monthCreate + getMonthArr[k] == month31Day[j]) {
                                if (getMonthArr[k] >= 2) {
                                    getDay = 0;
                                }
                                getDay = getDay + 31;
                            }
                        }
                        for (int j = 0; j < month30Day.length; j++) {
                            if (monthCreate + getMonthArr[k] == month30Day[j]) {
                                if (getMonthArr[k] >= 2) {
                                    getDay = 0;
                                }
                                getDay = getDay + 30;
                            }
                        }
                        if (monthCreate + getMonthArr[k] == month28DayOr29Day) {
                            if (yearCreate % 400 == 0) {
                                if (getMonthArr[k] >= 2) {
                                    getDay = 0;
                                }
                                getDay = getDay + 29;
                            } else if (yearCreate % 100 == 0) {
                                if (getMonthArr[k] >= 2) {
                                    getDay = 0;
                                }
                                getDay = getDay + 28;
                            } else if (yearCreate % 4 == 0) {
                                if (getMonthArr[k] >= 2) {
                                    getDay = 0;
                                }
                                getDay = getDay + 29;
                            } else {
                                if (getMonthArr[k] >= 2) {
                                    getDay = 0;
                                }
                                getDay = getDay + 28;
                            }
                        }
                        totalDay = totalDay + getDay;
                    }
                }
                int dayOfYear = 0;
                int totalDayOfYear = 0;
                if (getYear > 0) {
                    int[] getYearArr = new int[getYear];
                    if (getYear == 1) {
                        getYearArr[0] = 1;
                        if (shortNewsService.checkYear(yearCreate)) {
                            dayOfYear = 366;
                        } else {
                            dayOfYear = 365;
                        }
                    }
                    if (getYear >= 2) {
                        int value = yearCreate;
                        for (int j = 0; j < getYear; j++) {
                            getYearArr[j] = value;
                            if (shortNewsService.checkYear(getYearArr[j])) {
                                dayOfYear = 366;
                            } else {
                                dayOfYear = 365;
                            }
                            value++;
                            totalDayOfYear = totalDayOfYear + dayOfYear;
                        }
                    }
                }
                if (getMonth == 0 && getYear == 0) {
                    item.setRemaining(item.getExpired() - getDay);
                }
                if (getMonth > 0 && getYear == 0) {
                    item.setRemaining(item.getExpired() - totalDay);
                }
                if (getMonth == 0 && getYear == 1) {
                    item.setRemaining(item.getExpired() - dayOfYear);
                }
                if (getMonth == 0 && getYear > 1) {
                    item.setRemaining(item.getExpired() - totalDayOfYear);
                }
                if (getMonth > 0 && getYear > 0) {
                    int totalDayOfYearAndMonth = totalDayOfYear + totalDay;
                    item.setRemaining(item.getExpired() - totalDayOfYearAndMonth);
                }
            }
            shortNewsService.saveAll(shortNewsList);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Tạo tin
    @PostMapping("/createShortNews")
    public ResponseEntity<?> createShortNews(@RequestBody ShortNews shortNews,
                                             @RequestParam Long idUser) {

        if (userService.checkUser(idUser) == null) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Common.handlerWordsLanguage(shortNews);
        shortNewsService.createDefaultShortNews(shortNews);
        if (shortNews.getImage().equals(Constants.ImageDefault.DEFAULT_IMAGE_SHORT_NEW)) {
            if (StringUtils.isEmpty(shortNews.getContent())) {
                return new ResponseEntity<>(ResponseNotification.responseMessageDataField(Constants.DataField.CONTENT),
                        HttpStatus.BAD_REQUEST);
            }
        }
        if (StringUtils.isEmpty(shortNews.getStatus())) {
            shortNews.setStatus(Constants.STATUS_PUBLIC);
        }
        shortNews.setDelete(false);
        shortNews.setUser(userService.checkUser(idUser));
        shortNewsService.save(shortNews);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Chuyển vào thùng rác, Xóa tin trong database
    @DeleteMapping("/deleteShortNews")
    public ResponseEntity<?> deleteShortNews(@RequestParam Long idSortNew,
                                             @RequestParam Long idUser,
                                             @RequestParam String type) {
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<ShortNews> shortNewsOptional = shortNewsService.findById(idSortNew);
        if (shortNewsOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_SORT_NEW, idSortNew), HttpStatus.NOT_FOUND);
        }
        if ("trash".equals(type)) {
            if (userOptional.get().getId().equals(shortNewsOptional.get().getUser().getId())) {
                shortNewsOptional.get().setDelete(true);
                shortNewsService.save(shortNewsOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        if ("delete".equals(type)) {
            if (userOptional.get().getId().equals(shortNewsOptional.get().getUser().getId())) {
                shortNewsService.delete(shortNewsOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    // Xóa tất cả, khôi phục tất cả
    @DeleteMapping("/actionShortNews")
    public ResponseEntity<?> actionShortNews(@RequestParam List<Long> listIdSortNew,
                                             @RequestParam Long idUser,
                                             @RequestParam String type,
                                             @RequestHeader("Authorization") String authorization) {
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        boolean check = userService.errorToken(authorization, idUser);
        if (!check) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        List<ShortNews> shortNews = shortNewsRepository.findAllById(listIdSortNew);
        if (Constants.DELETE_ALL.equalsIgnoreCase(type)) {
            shortNewsRepository.deleteInBatch(shortNews);
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
    public ResponseEntity<?> shortNewsInTrash(@RequestParam Long idUser) {
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        List<ShortNews> shortNews = shortNewsService.getListShortNewInTrash(idUser);
        if (CollectionUtils.isEmpty(shortNews)) {
            shortNews = new ArrayList<>();
        }
        return new ResponseEntity<>(shortNews, HttpStatus.OK);
    }
}
