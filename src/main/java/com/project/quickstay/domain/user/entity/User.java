package com.project.quickstay.domain.user.entity;

import com.project.quickstay.common.Social;
import com.project.quickstay.domain.user.dto.UserRegister;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Entity
@Getter
@Table(name = "Users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String email;

    @Enumerated(value = EnumType.STRING)
    private Social social;

    @Enumerated(value = EnumType.STRING)
    private UserType userType;

    private String nickname;

    public User() {
    }

    public static User register(UserRegister userRegister) {
        User user = new User();
        user.userType = UserType.NEW_USER;
        user.email = userRegister.getEmail();
        user.social = userRegister.getSocial();
        user.nickname = userRegister.getNickname();
        return user;
    }

    public void changeUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", social=" + social +
                ", nickname='" + nickname + '\'' +
                '}';
    }

}