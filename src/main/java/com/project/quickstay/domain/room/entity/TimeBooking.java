package com.project.quickstay.domain.room.entity;

import com.project.quickstay.domain.room.dto.RoomData;
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

    @Override
    protected RoomData getUpdateData(RoomData roomData) {
        roomData.setBookType(getBookType());
        roomData.setStartTime(startTime);
        roomData.setEndTime(endTime);
        return roomData;
    }

    @Override
    protected void update(RoomData register) {
        this.startTime = register.getStartTime();
        this.endTime = register.getEndTime();
    }

    protected TimeBooking() {
    }

    protected TimeBooking(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
