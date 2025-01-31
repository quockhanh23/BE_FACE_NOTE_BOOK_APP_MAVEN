package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.controller;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Common;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.MessageResponse;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Regex;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.*;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.exeption.InvalidException;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.jwt.JWTService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.jwt.JwtResponse;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Image;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Role;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.User;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.VerificationToken;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.VerificationTokenRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.EmailService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.ImageService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.RoleService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final AuthenticationManager authenticationManager;

    private final VerificationTokenRepository verificationTokenRepository;

    private final JWTService jwtService;

    private final UserService userService;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final ImageService imageService;

    private final EmailService emailService;

    private final HttpServletRequest request;

    @Autowired
    public UserController(AuthenticationManager authenticationManager,
                          VerificationTokenRepository verificationTokenRepository,
                          JWTService jwtService,
                          UserService userService,
                          RoleService roleService,
                          PasswordEncoder passwordEncoder,
                          ModelMapper modelMapper,
                          ImageService imageService,
                          EmailService emailService,
                          HttpServletRequest request) {
        this.authenticationManager = authenticationManager;
        this.verificationTokenRepository = verificationTokenRepository;
        this.jwtService = jwtService;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.imageService = imageService;
        this.emailService = emailService;
        this.request = request;
    }

    @GetMapping("/saveHistoryLogin")
    public ResponseEntity<Object> saveHistoryLogin(@RequestParam Long idUserLogin) {
        String ipAddress = request.getRemoteAddr();
        userService.saveHistoryLogin(idUserLogin, ipAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/historyLogin")
    public ResponseEntity<Object> getListHistoryLogin() {
        return new ResponseEntity<>(userService.getListHistoryLogin(), HttpStatus.OK);
    }

    @GetMapping("/searchByFullNameOrEmail")
    public ResponseEntity<Object> searchByFullNameOrEmail(String search, @RequestParam Long idUserLogin) {
        List<UserBlackListDTO> list = new ArrayList<>();
        if (!StringUtils.isEmpty(search)) {
            List<User> userList = userService.findAllByEmailAndFullName(search, idUserLogin);
            if (!CollectionUtils.isEmpty(userList)) {
                userList.forEach(user -> {
                    UserBlackListDTO userBlackListDTO = new UserBlackListDTO();
                    BeanUtils.copyProperties(user, userBlackListDTO);
                    list.add(userBlackListDTO);
                });
            }
        }
        list.removeIf(item -> item.getId().equals(idUserLogin));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/searchAll")
    public ResponseEntity<Object> searchAll(String search, @RequestParam Long idUserLogin) {
        search = Common.addEscapeOnSpecialCharactersWhenSearch(search);
        List<UserSearchDTO> list = new ArrayList<>();
        List<User> userList = userService.searchAll(search, idUserLogin);
        if (!CollectionUtils.isEmpty(userList)) {
            userList.forEach(user -> {
                UserSearchDTO userDTO = new UserSearchDTO();
                BeanUtils.copyProperties(user, userDTO);
                list.add(userDTO);
            });
        }
        list.removeIf(item -> item.getId().equals(idUserLogin));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // Tìm kiếm bạn bè của người dùng
    @GetMapping("/searchFriend")
    public ResponseEntity<Object> searchFriend(@RequestParam String search, @RequestParam Long idUser) {
        search = Common.addEscapeOnSpecialCharactersWhenSearch(search);
        List<UserDTO> userDTOList = userService.listFriend(idUser);
        List<User> friend = userService.searchFriend(search, idUser);
        List<UserDTO> list = new ArrayList<>();
        List<Long> idList = friend.stream().map(User::getId).toList();
        for (Long id : idList) {
            userDTOList.stream().filter(item -> item.getId().equals(id)).findFirst().ifPresent(list::add);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // Đăng kí tài khoản
    @Transactional()
    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            Map<String, String> fieldError = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                fieldError.put(error.getField(), error.getDefaultMessage());
            }
            throw new InvalidException(MessageResponse.IN_VALID, fieldError);
        }
        Common.handlerWordsLanguage(user);
        if (!Common.checkRegex(user.getUsername(), Regex.CHECK_USER_NAME)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.RegisterMessage.NO_VALID_USER_NAME),
                    HttpStatus.BAD_REQUEST);
        }
        if (!userService.isCorrectConfirmPassword(user)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.RegisterMessage.WRONG_CONFIRM_PASSWORD),
                    HttpStatus.BAD_REQUEST);
        }
        ResponseNotification responseNotification = userService.checkExistUserNameAndEmail(user);
        if (responseNotification != null) return new ResponseEntity<>(responseNotification, HttpStatus.BAD_REQUEST);

        Set<Role> roles = new HashSet<>();
        Role role;
        role = (user.getRoles() != null) ?
                roleService.findByName(Constants.Roles.ROLE_ADMIN) : roleService.findByName(Constants.Roles.ROLE_USER);
        roles.add(role);
        user.setRoles(roles);
        userService.createDefault(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        user.setStatus(Constants.STATUS_ACTIVE);
        userService.save(user);
        List<String> listAvatar = Arrays.asList(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_MALE,
                Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_LGBT, Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_FEMALE);
        List<String> listCover = Arrays.asList(Constants.ImageDefault.DEFAULT_BACKGROUND,
                Constants.ImageDefault.DEFAULT_BACKGROUND_2, Constants.ImageDefault.DEFAULT_BACKGROUND_3);
        if (!listAvatar.contains(user.getAvatar())) {
            Image image = imageService.createImageDefault(user.getAvatar(), user);
            imageService.save(image);
        }
        if (!listCover.contains(user.getCover())) {
            Image image = imageService.createImageDefault(user.getCover(), user);
            imageService.save(image);
        }
//        emailService.sendMail(user.getEmail(), MessageResponse.Email.WELL_COME + user.getFullName(),
//                MessageResponse.Email.THANK);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // Lấy lại mật khẩu
    @Transactional
    @PostMapping("/passwordRetrieval")
    public ResponseEntity<Object> passwordRetrieval(@RequestBody PasswordRetrieval passwordRetrieval,
                                                    @RequestHeader("Authorization") String authorization) {
        if (StringUtils.isEmpty(passwordRetrieval.getUserName()) || StringUtils.isEmpty(passwordRetrieval.getEmail())) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.IN_VALID), HttpStatus.BAD_REQUEST);
        }
        Optional<User> userOptional = userService
                .findUserByEmailAndUserName(passwordRetrieval.getUserName(), passwordRetrieval.getEmail());
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.NOT_FOUND.toString(),
                    MessageResponse.RegisterMessage.PASSWORD_RETRIEVAL_FAIL),
                    HttpStatus.NOT_FOUND);
        }
        boolean check = userService.errorToken(authorization, userOptional.get().getId());
        if (!check) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        try {
            String newPassword = RandomStringUtils.randomAscii(6);
            userOptional.get().setPassword(passwordEncoder.encode(newPassword));
            userService.save(userOptional.get());
            emailService.sendMail(userOptional.get().getEmail(),
                    MessageResponse.Email.RESET_PASSWORD + MessageResponse.Email.SPACE + MessageResponse.Email.APP,
                    MessageResponse.Email.NEW_PASSWORD + newPassword);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.OK.toString(),
                    e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.OK.toString(),
                MessageResponse.RegisterMessage.PASSWORD_RETRIEVAL + passwordRetrieval.getEmail()),
                HttpStatus.OK);
    }

    // Đổi mật khẩu
    @Transactional()
    @PostMapping("/matchPassword")
    public ResponseEntity<Object> matchPassword(@RequestBody UserChangePassword userChangePassword,
                                                @RequestParam Long idUser,
                                                @SuppressWarnings("unused")
                                                @RequestHeader("Authorization") String authorization) {
        try {
            User user = userService.checkExistUser(idUser);
            if (passwordEncoder.matches(userChangePassword.getPasswordOld(), user.getPassword())) {
                if (userChangePassword.getPasswordNew().equals(userChangePassword.getConfirmPasswordNew())) {
                    user.setPassword(passwordEncoder.encode(userChangePassword.getPasswordNew()));
                    userService.save(user);
                    return new ResponseEntity<>(user, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                            MessageResponse.RegisterMessage.WRONG_CONFIRM_PASSWORD),
                            HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.RegisterMessage.WRONG_PASSWORD),
                    HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        try {
            List<User> userBannedList = userService.findAllUserBanned();
            if (!CollectionUtils.isEmpty(userBannedList)) {
                if (userBannedList.stream().parallel().anyMatch(item -> item.getUsername().equals(user.getUsername()))) {
                    return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                            MessageResponse.LoginMessage.USER_HAS_LOCK),
                            HttpStatus.BAD_REQUEST);
                }
            }
            Optional<User> currentUser = userService.findByUsername(user.getUsername());
            if (currentUser.isEmpty()) {
                return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                        MessageResponse.LoginMessage.USER_NAME),
                        HttpStatus.BAD_REQUEST);
            }

            Authentication authentication;
            try {
                authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                        MessageResponse.RegisterMessage.WRONG_PASSWORD),
                        HttpStatus.BAD_REQUEST);
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);
            Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository
                    .findByUserId(currentUser.get().getId());
            if (verificationTokenOptional.isEmpty()) {
                VerificationToken verificationToken = new VerificationToken();
                verificationToken.setToken(jwt);
                verificationToken.setCreatedDate(new Date());
                verificationToken.setUser(currentUser.get());
                verificationTokenRepository.save(verificationToken);
            } else {
                verificationTokenOptional.get().setCreatedDate(new Date());
                verificationTokenOptional.get().setToken(jwt);
                verificationTokenRepository.save(verificationTokenOptional.get());
            }
            return ResponseEntity.ok(new JwtResponse(jwt, currentUser.get().getId(),
                    userDetails.getUsername(), userDetails.getAuthorities()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users/{idUser}")
    public ResponseEntity<Object> getProfile(@PathVariable Long idUser) {
        User user = userService.checkExistUser(idUser);
        List<User> userBannedList = userService.findAllUserBanned();
        if (!CollectionUtils.isEmpty(userBannedList)) {
            if (userBannedList.stream().anyMatch(item -> item.getUsername().equals(user.getUsername()))) {
                return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                        MessageResponse.LoginMessage.USER_HAS_LOCK),
                        HttpStatus.BAD_REQUEST);
            }
        }
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/findNameUserById/{idUser}")
    public ResponseEntity<Object> findNameUserById(@PathVariable Long idUser) {
        User user = userService.checkExistUser(idUser);
        NameDTO nameDTO = modelMapper.map(user, NameDTO.class);
        return new ResponseEntity<>(nameDTO, HttpStatus.OK);
    }

    // Sửa thông tin người dùng
    @Transactional()
    @PutMapping("/users/{idUser}")
    public ResponseEntity<Object> updateUserProfile(@PathVariable Long idUser,
                                                    @RequestBody User user,
                                                    @SuppressWarnings("unused")
                                                    @RequestHeader("Authorization") String authorization) {
        if (StringUtils.isEmpty(user.getFullName()) || StringUtils.isEmpty(user.getPhone())) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.DESCRIPTION_BLANK), HttpStatus.BAD_REQUEST);
        }
        if (!Common.checkRegex(user.getPhone(), Regex.CHECK_NUMBER_PHONE)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.WRONG_NUMBER_PHONE), HttpStatus.BAD_REQUEST);
        }
        Common.handlerWordsLanguage(user);
        User userOptional = userService.checkExistUser(idUser);
        String avatarName = "";
        boolean checkCover = false;
        List<ListAvatarDefault> listAvatarDefaults = userService.listAvatar();
        for (ListAvatarDefault listAvatarDefault : listAvatarDefaults) {
            if (user.getAvatar().equalsIgnoreCase(listAvatarDefault.getName())) {
                avatarName = listAvatarDefault.getName();
            }
            if (user.getCover().equalsIgnoreCase(listAvatarDefault.getName())) {
                checkCover = true;
            }
        }
        if (StringUtils.isEmpty(avatarName)) {
            if (StringUtils.isNotEmpty(user.getAvatar())
                    && !user.getAvatar().equalsIgnoreCase(userOptional.getAvatar())) {
                userOptional.setAvatar(user.getAvatar());
                try {
                    Image image = imageService.createImageDefault(user.getAvatar(), user);
                    imageService.save(image);
                    userService.saveImageUserLogin(idUser, user.getAvatar());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            userOptional.setAvatar(avatarName);
            userService.saveImageUserLogin(idUser, avatarName);
        }
        if (!StringUtils.isEmpty(user.getCover())
                && !userOptional.getCover().equals(user.getCover())
                && !checkCover) {
            userOptional.setCover(user.getCover());
            Image image = imageService.createImageDefault(user.getCover(), user);
            imageService.save(image);
        }
        if (!StringUtils.isEmpty(user.getGender())) {
            userOptional.setGender(user.getGender());
        }
        if (user.getDateOfBirth() != null) {
            userOptional.setDateOfBirth(user.getDateOfBirth());
        }
        userOptional.setAddress(user.getAddress());
        userOptional.setPhone(user.getPhone());
        userOptional.setFavorite(user.getFavorite());
        userOptional.setEducation(user.getEducation());
        userService.save(userOptional);
        return new ResponseEntity<>(userOptional, HttpStatus.OK);
    }

    @DeleteMapping("/changeStatusUser")
    public ResponseEntity<Object> changeStatusUser(@RequestParam Long idUser,
                                                   @RequestParam String type,
                                                   @SuppressWarnings("unused")
                                                   @RequestHeader("Authorization") String authorization) {
        User user = userService.checkExistUser(idUser);
        if ("active".equalsIgnoreCase(type)) {
            if (user.getStatus().equals(Constants.STATUS_ACTIVE)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            if (user.getId().equals(idUser)) {
                user.setStatus(Constants.STATUS_ACTIVE);
                userService.save(user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        }
        if ("lock".equalsIgnoreCase(type)) {
            if (user.getStatus().equals(Constants.STATUS_LOCK)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            if (user.getId().equals(idUser)) {
                user.setStatus(Constants.STATUS_LOCK);
                userService.save(user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/listImageDefault")
    public ResponseEntity<Object> listImageDefault() {
        return new ResponseEntity<>(userService.listAvatar(), HttpStatus.OK);
    }

    @DeleteMapping("/changeImage")
    public ResponseEntity<Object> changeImage(@RequestParam Long idUser,
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
        if (Constants.AVATAR.equals(type)) {
            user.setAvatar(imageOptional.get().getLinkImage());
            userService.saveImageUserLogin(idUser, imageOptional.get().getLinkImage());
        }
        if (Constants.COVER.equals(type)) {
            user.setCover(imageOptional.get().getLinkImage());
        }
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/removeToken")
    public ResponseEntity<Object> removeToken(@RequestParam Long idUserLogin) {
        if (idUserLogin == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByUserId(idUserLogin);
        if (verificationToken.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        verificationToken.get().setToken("no token");
        verificationTokenRepository.save(verificationToken.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
