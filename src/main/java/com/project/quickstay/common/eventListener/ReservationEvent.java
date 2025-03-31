package com.project.quickstay.common.eventListener;

import com.project.quickstay.domain.room.entity.Room;
import lombok.Getter;

@Getter
public class ReservationEvent {

    private final Room room;

    public ReservationEvent(Room room) {
        this.room = room;
    }
}
