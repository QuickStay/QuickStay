package com.project.quickstay.domain;

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
}
