package com.project.quickstay.domain.room.entity;

import com.project.quickstay.domain.room.dto.RoomData;

public enum BookType {
    TIME {
        @Override
        public Booking createBooking(RoomData roomData) {
            return new TimeBooking(roomData.getStartTime(), roomData.getEndTime());
        }
    },
    DAY {
        @Override
        public Booking createBooking(RoomData roomData) {
            return new DayBooking(roomData.getCheckIn(), roomData.getCheckOut());
        }
    };

    public abstract Booking createBooking(RoomData roomData);
}
