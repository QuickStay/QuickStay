    package com.project.quickstay.domain.reservation.entity;

import com.project.quickstay.common.State;
import com.project.quickstay.domain.reservation.dto.DayReservationRegister;
import com.project.quickstay.domain.reservation.dto.ReservationDTO;
import com.project.quickstay.domain.reservation.dto.TimeReservationRegister;
import com.project.quickstay.domain.room.dto.RoomData;
import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.domain.room.entity.Booking;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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

    @Enumerated(value = EnumType.STRING)
    private State state;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    public Reservation() {
    }


    public static Reservation register(User user, Room room, ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();
        reservation.room = room;
        reservation.user = user;
        reservation.state = State.RESERVED;

        if (room.getBooking().getBookType() == BookType.DAY) {
            DayReservationRegister reservationRegister = (DayReservationRegister)reservationDTO;
            reservation.startDate = reservationRegister.getStartDate();
            reservation.endDate = reservationRegister.getEndDate();
        }
        else if (room.getBooking().getBookType() == BookType.TIME) {
            TimeReservationRegister reservationRegister = (TimeReservationRegister)reservationDTO;
            reservation.startDate = reservationRegister.getDate();
            reservation.endDate = reservationRegister.getDate();
            reservation.startTime = reservationRegister.getStartTime();
            reservation.endTime = reservationRegister.getEndTime();
        }

        return reservation;
    }


    public void updateState(State state) {
        this.state = state;
    }
}
