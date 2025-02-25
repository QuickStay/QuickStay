package com.project.quickstay.domain.room.entity;

import com.project.quickstay.domain.room.dto.RoomData;
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

    @Override
    protected RoomData getUpdateData(RoomData roomData) {
        roomData.setBookType(getBookType());
        roomData.setCheckIn(checkIn);
        roomData.setCheckOut(checkOut);
        return roomData;
    }

    @Override
    protected Booking update(RoomData register) {
        this.checkIn = register.getCheckIn();
        this.checkOut = register.getCheckOut();
        return this;
    }

    protected DayBooking() {
    }

    protected DayBooking(LocalTime checkIn, LocalTime checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    protected void setTime(LocalTime checkIn, LocalTime checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
