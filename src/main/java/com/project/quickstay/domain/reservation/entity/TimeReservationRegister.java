package com.project.quickstay.domain.reservation.entity;

import com.project.quickstay.common.State;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TimeReservationRegister extends ReservationRegister {
    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    public TimeReservationRegister(User user, Room room) {
        super(user, room);
    }

    @Override
    public Reservation createReservation() {
        return Reservation.builder()
                .startDate(date)
                .endDate(date)
                .startTime(startTime)
                .endTime(endTime)
                .state(State.RESERVED)
                .room(getRoom())
                .user(getUser())
                .build();
    }
}
