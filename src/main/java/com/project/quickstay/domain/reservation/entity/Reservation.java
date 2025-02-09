    package com.project.quickstay.domain.reservation.entity;

import com.project.quickstay.common.State;
import com.project.quickstay.domain.reservation.dto.DayReservationRegister;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.entity.User;
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
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(value = EnumType.STRING)
    private State state;

    private LocalTime startTime;

    private LocalTime endTime;

    public Reservation() {
    }

    public static Reservation dayRegister(User user, Room room, DayReservationRegister dayReservationRegister) {
        Reservation reservation = new Reservation();
        reservation.user = user;
        reservation.room = room;
        reservation.startDate = dayReservationRegister.getStartDate();
        reservation.endDate = dayReservationRegister.getEndDate();
        return reservation;
    }
}
