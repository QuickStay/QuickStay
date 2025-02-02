package com.project.quickstay.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    private String name;

    private String description;

    private Integer capacity;

    public Room() {

    }
}
