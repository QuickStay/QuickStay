package com.project.quickstay.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDate date;

    @Enumerated(value = EnumType.STRING)
    private State state;

    private LocalTime startTime;

    private LocalTime endTime;

    public Reservation() {
    }
}
