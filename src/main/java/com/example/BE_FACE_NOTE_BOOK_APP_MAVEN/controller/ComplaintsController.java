package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.UserComplaints;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.UserComplaintsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/comps")
@Slf4j
public class ComplaintsController {

    private final UserComplaintsService userComplaintsService;

    @Autowired
    public ComplaintsController(UserComplaintsService userComplaintsService) {
        this.userComplaintsService = userComplaintsService;
    }

    @GetMapping("/getAllByUserId")
    public ResponseEntity<Object> getAllByUserId(@RequestParam Long idUser) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteComp")
    public ResponseEntity<Object> deleteComplaints(@RequestParam Long idUser) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Object> saveComplaints(@RequestBody UserComplaints userComplaints) {
        userComplaints.setCreatedAt(new Date());
        userComplaints.setStatus("");
        userComplaintsService.save(userComplaints);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
