package com.project.quickstay.domain.reservation.entity;

import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.entity.User;
import lombok.Getter;

@Getter
public abstract class ReservationRegister {
    private User user;
    private Room room;

    public void placeUser(User user) {
        this.user = user;
    }

    public void placeRoom(Room room) {
        this.room = room;
    }

    public ReservationRegister(User user, Room room) {
        this.user = user;
        this.room = room;
    }

    public abstract Reservation createReservation();
}