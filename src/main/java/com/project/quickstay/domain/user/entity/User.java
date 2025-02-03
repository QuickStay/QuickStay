package com.project.quickstay.domain.user.entity;

import com.project.quickstay.common.Social;
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

    private String phoneNumber;

    public User() {
    }
}
