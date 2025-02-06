package com.project.quickstay.domain.booking.entity;

import com.project.quickstay.domain.room.entity.Room;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalTime;

@Entity
@Getter
@DiscriminatorValue("TIME")
public class TimeBooking extends Booking {

    private LocalTime startTime;
    private LocalTime endTime;

    public TimeBooking() {
    }

    public TimeBooking(Room room, LocalTime startTime, LocalTime endTime) {
        super(room);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
