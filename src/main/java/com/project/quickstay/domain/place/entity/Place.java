package com.project.quickstay.domain.place.entity;

import com.project.quickstay.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String name;

    private String description;

    private String address;

    public Place() {
    }
}
