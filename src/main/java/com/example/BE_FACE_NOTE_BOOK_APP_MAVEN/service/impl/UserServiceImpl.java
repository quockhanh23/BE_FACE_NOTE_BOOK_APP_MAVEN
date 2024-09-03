package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.impl;


import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Common;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.MessageResponse;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.ListAvatarDefault;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto.UserDTO;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.jwt.JWTService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.*;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.notification.ResponseNotification;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.object.AvatarDefault;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.FollowWatchingRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.LastUserLoginRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.UserRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.VerificationTokenRepository;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.FriendRelationService;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowWatchingRepository followWatchingRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FriendRelationService friendRelationService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private LastUserLoginRepository lastUserLoginRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException(username);
            }
            if (this.checkLogin(user)) {
                return UserPrinciple.build(user);
            }
            boolean enable = false;
            boolean accountNonExpired = false;
            boolean credentialsNonExpired = false;
            boolean accountNonLocked = false;
            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(), enable, accountNonExpired, credentialsNonExpired,
                    accountNonLocked, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @CacheEvict(cacheNames = {"allFriendByUserId", "findAllUserBanned", "listPeople", "friendSuggestion",
            "findAllByIdIn", "findAllUser", "findAllRoleUser"}, allEntries = true)
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @Cacheable(cacheNames = "findAllRoleUser")
    public List<User> findAllRoleUser() {
        return userRepository.findAllRoleUser();
    }

    @Override
    @Cacheable(cacheNames = "findAllUser")
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    @Override
    public Optional<User> getCurrentUser() {
        Optional<User> user;
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        user = this.findByUsername(userName);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDetails loadUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NullPointerException();
        }
        return UserPrinciple.build(user.get());
    }

    @Override
    public boolean checkLogin(User user) {
        Iterable<User> users = this.findAll();
        boolean isCorrectUser = false;
        for (User currentUser : users) {
            if (currentUser.getUsername().equals(user.getUsername())
                    && user.getPassword().equals(currentUser.getPassword()) &&
                    currentUser.isEnabled()) {
                isCorrectUser = true;
                break;
            }
        }
        return isCorrectUser;
    }

    @Override
    public boolean isCorrectConfirmPassword(User user) {
        return user.getPassword().equals(user.getConfirmPassword());
    }

    @Override
    public void createDefault(User user) {
        if (user.getAvatar().equals(Constants.ImageDefault.DEFAULT_BACKGROUND_2)) {
            user.setAvatar(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_MALE);
        }
        if (user.getAvatar().equals(Constants.ImageDefault.DEFAULT_BACKGROUND_2)) {
            user.setAvatar(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_MALE);
        }
        if (user.getGender().equals(Constants.GENDER_FEMALE)) {
            user.setAvatar(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_FEMALE);
        }
        if (user.getGender().equals(Constants.BLANK)) {
            user.setGender(Constants.GENDER_DEFAULT);
        }
        if (user.getGender().equals(Constants.GENDER_DEFAULT)) {
            user.setAvatar(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_LGBT);
        }
    }

    @Override
    public UserDTO mapper(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public Object checkAdmin(Long idAdmin) {
        if (idAdmin == null) return null;
        Optional<User> adminOptional = userRepository.findById(idAdmin);
        if (adminOptional.isPresent()) {
            Set<Role> role = adminOptional.get().getRoles();
            if (role.stream().anyMatch(item -> item.getName().equalsIgnoreCase(Constants.Roles.ROLE_ADMIN))) {
                return adminOptional.get().getRoles();
            }
        }
        return null;
    }

    @Override
    public User checkUser(Long idUser) {
        Optional<User> userOptional = userRepository.findById(idUser);
        return userOptional.orElse(null);
    }

    @Override
    public List<ListAvatarDefault> listAvatar() {
        List<ListAvatarDefault> listImageDefault = new ArrayList<>();
        AvatarDefault avatarDefault = new AvatarDefault();
        listImageDefault.add(new ListAvatarDefault(1L, avatarDefault.getDEFAULT_AVATAR_1(), Constants.GENDER_MALE));
        listImageDefault.add(new ListAvatarDefault(2L, avatarDefault.getDEFAULT_AVATAR_2(), Constants.GENDER_MALE));
        listImageDefault.add(new ListAvatarDefault(3L, Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_MALE, Constants.GENDER_MALE));
        listImageDefault.add(new ListAvatarDefault(4L, Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_LGBT, Constants.GENDER_DEFAULT));
        listImageDefault.add(new ListAvatarDefault(5L, avatarDefault.getDEFAULT_AVATAR_3(), Constants.GENDER_FEMALE));
        listImageDefault.add(new ListAvatarDefault(6L, Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_FEMALE, Constants.GENDER_FEMALE));
        listImageDefault.add(new ListAvatarDefault(7L, avatarDefault.getDEFAULT_AVATAR_4(), Constants.GENDER_FEMALE));
        listImageDefault.add(new ListAvatarDefault(8L, avatarDefault.getDEFAULT_AVATAR_5(), Constants.GENDER_FEMALE));
        listImageDefault.add(new ListAvatarDefault(9L, avatarDefault.getDEFAULT_AVATAR_6(), Constants.GENDER_FEMALE));
        return listImageDefault;
    }

    @Override
    @Cacheable(cacheNames = "findAllByIdIn",
            key = "#root.methodName + '_' + T(com.example.utils.CacheKeyGenerator).generateKey(#inputList)")
    public Set<User> findAllByIdIn(Set<Long> inputList) {
        return userRepository.findAllByIdIn(inputList);
    }

    @Override
    @Cacheable(cacheNames = "friendSuggestion", key = "#idUser")
    public List<User> friendSuggestion(Long idUser) {
        return userRepository.friendSuggestion(idUser);
    }

    @Override
    @Cacheable(cacheNames = "listPeople", key = "#idUser")
    public List<User> listPeople(Long idUser) {
        return userRepository.listPeople(idUser);
    }

    @Override
    @Cacheable(cacheNames = "findAllUserBanned")
    public List<User> findAllUserBanned() {
        return userRepository.findAllUserBanned();
    }

    @Override
    @Cacheable(cacheNames = "allFriendByUserId", key = "#idUser")
    public List<User> allFriendByUserId(Long idUser) {
        return userRepository.allFriendByUserId(idUser);
    }

    @Override
    public List<UserDTO> filterUser(Long idUser, String type) {
        List<Long> longList = new ArrayList<>();
        List<UserDTO> userDTOList = new ArrayList<>();
        if (type.equals(Constants.FollowPeople.FOLLOW)) {
            List<FollowWatching> followList = followWatchingRepository.getListFollowByIdUser(idUser);
            longList = followList.stream().map(FollowWatching::getIdUserTarget).collect(Collectors.toList());
        }
        if (type.equals(Constants.FollowPeople.WATCHING)) {
            List<FollowWatching> watchingList = followWatchingRepository.getListWatchingByIdUser(idUser);
            longList = watchingList.stream().map(FollowWatching::getIdUser).collect(Collectors.toList());
        }
        List<User> list = userRepository.listPeopleNoImpact(idUser);
        List<User> listUserFilter = new ArrayList<>();
        if (CollectionUtils.isEmpty(longList)) return userDTOList;
        for (Long id : longList) {
            list.stream().filter(item -> item.getId().equals(id)).findFirst().ifPresent(listUserFilter::add);
        }
        List<User> listFriend = allFriendByUserId(idUser);
        if (CollectionUtils.isEmpty(listFriend)) listFriend = new ArrayList<>();
        for (User value : listUserFilter) {
            UserDTO userDTO = modelMapper.map(value, UserDTO.class);
            userDTOList.add(userDTO);
        }
        for (int i = 0; i < userDTOList.size(); i++) {
            List<User> friendOfUserFilter = allFriendByUserId(userDTOList.get(i).getId());
            List<User> mutualFriends = new ArrayList<>();
            if (!CollectionUtils.isEmpty(friendOfUserFilter)) {
                for (int j = 0; j < listFriend.size(); j++) {
                    Long idUserFriend = listFriend.get(j).getId();
                    friendOfUserFilter.stream().filter(item -> item.getId().equals(idUserFriend)).
                            findFirst().ifPresent(mutualFriends::add);
                }
            }
            userDTOList.get(i).setMutualFriends(mutualFriends.size());
        }
        List<FriendRelation> friendRelationList = friendRelationService.listRequest(idUser);
        if (!CollectionUtils.isEmpty(friendRelationList)) {
            Set<Long> listUserId = new HashSet<>();
            for (FriendRelation friendRelation : friendRelationList) {
                listUserId.add(friendRelation.getIdFriend());
            }
            Set<User> userList = findAllByIdIn(listUserId);
            if (userList == null) userList = new HashSet<>();
            List<User> checkUserFollowSendRequestFriend = new ArrayList<>(userList);
            for (int i = 0; i < checkUserFollowSendRequestFriend.size(); i++) {
                Long id = checkUserFollowSendRequestFriend.get(i).getId();
                userDTOList.stream().filter(item -> item.getId().equals(id)).findFirst().ifPresent(user -> {
                    user.setSendRequestFriend(true);
                });
            }
        }
        List<FriendRelation> friendRelations = friendRelationService.findAllListRequestAddFriendById(idUser);
        List<Optional<User>> optionalList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(friendRelations)) {
            for (FriendRelation friendRelation : friendRelations) {
                Optional<User> userOptional = findById(friendRelation.getFriend().getId());
                if (userOptional.isEmpty() || userOptional.get().getId().equals(idUser)) continue;
                optionalList.add(userOptional);
            }
        }
        if (!CollectionUtils.isEmpty(optionalList)) {
            for (int i = 0; i < optionalList.size(); i++) {
                if (optionalList.get(i).isPresent()) {
                    Long id = optionalList.get(i).get().getId();
                    userDTOList.stream().filter(item -> item.getId().equals(id)).findFirst()
                            .ifPresent(user -> user.setPeopleSendRequestFriend(true));
                }
            }
        }
        return userDTOList;
    }

    @Override
    public List<User> findAllByIdIn(List<Long> inputList) {
        return userRepository.findAllByIdIn(inputList);
    }

    @Override
    public List<User> findAllByEmailAndFullName(String searchText, Long idSendBlock) {
        return userRepository.findAllByEmailAndFullName(searchText, idSendBlock);
    }

    @Override
    public List<User> searchFriend(String searchText, Long idUser) {
        return userRepository.searchFriend(searchText, idUser);
    }

    @Override
    public List<User> searchAll(String searchText, Long idUser) {
        return userRepository.searchAll(searchText, idUser);
    }

    @Override
    public ResponseNotification checkExistUserNameAndEmail(User user) {
        List<User> list = userRepository.findAll();
        List<String> userName = list.stream().map(User::getUsername).toList();
        if (userName.stream().anyMatch(item -> item.equals(user.getUsername()))) {
            return new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.RegisterMessage.USER_NAME_DUPLICATE);
        }
        List<String> email = list.stream().map(User::getEmail).toList();
        if (email.stream().parallel().anyMatch(item -> item.equals(user.getEmail()))) {
            return new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.RegisterMessage.EMAIL_USED);
        }
        return null;
    }

    @Override
    public Optional<User> findUserByEmailAndUserName(String userName, String email) {
        return userRepository.findUserByEmailAndUserName(userName, email);
    }

    public boolean errorToken(String authorization, Long idUser) {
        try {
            if (StringUtils.isEmpty(authorization)) return false;
            String tokenRequest = Common.formatToken(authorization);
            String userName = jwtService.extractUsername(tokenRequest);
            Optional<VerificationToken> verificationToken = verificationTokenRepository.findByUserId(idUser);
            return verificationToken.filter(token -> tokenRequest.equals(token.getToken())
                    && !"no token".equals(token.getToken())
                    && userName.equals(token.getUser().getUsername())).isPresent();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> findAllByEmailOrUsername(String searchText) {
        return userRepository.findAllByEmailOrUsername(searchText);
    }

    @Override
    public List<UserDTO> copyListDTO(List<User> users) {
        List<UserDTO> userDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(users)) return userDTOList;
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    @Override
    public void saveImageUserLogin(Long idUser, String image) {
        LastUserLogin lastUserLogin = lastUserLoginRepository.findByIdUser(idUser);
        if (Objects.isNull(lastUserLogin)) return;
        lastUserLogin.setAvatar(image);
        lastUserLoginRepository.save(lastUserLogin);
    }

    @Override
    public List<UserDTO> listFriend(Long idUser) {
        List<User> listFriend = allFriendByUserId(idUser);
        List<UserDTO> userDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(listFriend)) return userDTOList;
        listFriend.forEach(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            List<User> friendOfFriend = allFriendByUserId(user.getId());
            List<User> mutualFriends = new ArrayList<>();
            if (!CollectionUtils.isEmpty(friendOfFriend)) {
                List<Long> listId = userDTOList.stream().map(UserDTO::getId).toList();
                for (Long id : listId) {
                    friendOfFriend.stream().filter(item -> item.getId().equals(id))
                            .findFirst().ifPresent(mutualFriends::add);
                }
            }
            userDTO.setMutualFriends(mutualFriends.size());
            userDTOList.add(userDTO);
        });
        return userDTOList;
    }
}
