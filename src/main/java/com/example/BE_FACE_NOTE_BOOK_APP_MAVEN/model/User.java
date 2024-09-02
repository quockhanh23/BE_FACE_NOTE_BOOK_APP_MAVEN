package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userTable")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false, unique = true)
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;

    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles;

    private String fullName;
    private String email;
    @Column(length = 50)
    private String phone;
    @Column(length = 500)
    private String favorite;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "Phải là ngày trong quá khứ")
    private Date dateOfBirth;
    @Column(length = 1000)
    private String avatar = "assets/images/defaultAva.png";

    @Column(length = 1000)
    private String cover = "assets/images/face-map_ccexpress.png";

    private String address;

    private String status;

    private String gender;

    private String education;
    private Date createAt = new Date();
}
