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
    protected void update(RoomData register) {
        this.checkIn = register.getCheckIn();
        this.checkOut = register.getCheckOut();
    }

    public DayBooking() {
    }

    protected DayBooking(LocalTime checkIn, LocalTime checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public void setTime(LocalTime checkIn, LocalTime checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
