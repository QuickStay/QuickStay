package com.project.quickstay.domain.reservation.entity;

import com.project.quickstay.common.State;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DayReservationRegister extends ReservationRegister {

    private LocalDate startDate;

    private LocalDate endDate;

    public DayReservationRegister(User user, Room room) {
        super(user, room);
    }

    @Override
    public Reservation createReservation() {
        return Reservation
                .builder()
                .startDate(startDate)
                .endDate(endDate)
                .state(State.RESERVED)
                .room(getRoom())
                .user(getUser())
                .build();
    }
}
