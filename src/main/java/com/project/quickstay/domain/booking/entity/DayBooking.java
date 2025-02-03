package com.project.quickstay.domain.booking.entity;

import com.project.quickstay.domain.room.entity.Room;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@DiscriminatorValue("DAY")
public class DayBooking extends Booking {

    private LocalDate checkIn;
    private LocalDate checkOut;

    public DayBooking() {
    }

    public DayBooking(Room room, LocalDate checkIn, LocalDate checkOut) {
        super(room);
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
