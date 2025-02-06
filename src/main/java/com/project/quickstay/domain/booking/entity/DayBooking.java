package com.project.quickstay.domain.booking.entity;

import com.project.quickstay.domain.room.entity.Room;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalTime;

@Entity
@Getter
@DiscriminatorValue("DAY")
public class DayBooking extends Booking {

    private LocalTime checkIn;
    private LocalTime checkOut;

    public DayBooking() {
    }

    public DayBooking(Room room, LocalTime checkIn, LocalTime checkOut) {
        super(room);
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public void setTime(LocalTime checkIn, LocalTime checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
