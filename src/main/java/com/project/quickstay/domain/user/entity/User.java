package com.project.quickstay.domain.user.entity;

import com.project.quickstay.common.Social;
import com.project.quickstay.domain.user.dto.UserRegister;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Enumerated(value = EnumType.STRING)
    private Social social;

    private String nickname;

    public User() {
    }

    public static User register(UserRegister userRegister) {
        User user = new User();
        user.email = userRegister.getEmail();
        user.social = userRegister.getSocial();
        user.nickname = userRegister.getNickname();
        return user;
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