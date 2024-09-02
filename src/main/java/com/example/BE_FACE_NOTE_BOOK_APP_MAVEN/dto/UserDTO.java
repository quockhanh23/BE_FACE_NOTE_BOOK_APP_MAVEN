package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String favorite;
    private String avatar;
    private String cover;
    private String address;
    private String gender;
    private String education;
    // ACCEPT_CASE_INSENSITIVE_VALUES: json không đọc được LocalDate, đã thử nhiều cách nhưng không được
    private Date dateOfBirth;

    private Date createAt;
    private String status;
    private long mutualFriends;
    private Boolean sendRequestFriend = false;
    private Boolean peopleSendRequestFriend = false;
    private long numberRepost;

    public UserDTO(Long id, String fullName, String avatar, String cover) {
        this.id = id;
        this.fullName = fullName;
        this.avatar = avatar;
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", favorite='" + favorite + '\'' +
                ", avatar='" + avatar + '\'' +
                ", cover='" + cover + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", education='" + education + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", createAt=" + createAt +
                ", status='" + status + '\'' +
                ", mutualFriends=" + mutualFriends +
                ", sendRequestFriend=" + sendRequestFriend +
                ", peopleSendRequestFriend=" + peopleSendRequestFriend +
                ", numberRepost=" + numberRepost +
                '}';
    }
}
