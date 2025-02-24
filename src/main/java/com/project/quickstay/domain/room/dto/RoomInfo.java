package com.project.quickstay.domain.room.dto;

import com.project.quickstay.common.BookType;
import com.project.quickstay.domain.room.entity.DayBooking;
import com.project.quickstay.domain.room.entity.TimeBooking;
import com.project.quickstay.domain.room.entity.Room;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class RoomInfo {

    private Long id;
    private String name;
    private String description;
    private int capacity;

    private BookType bookType;

    private LocalTime checkIn;
    private LocalTime checkOut;

    private LocalTime startTime;
    private LocalTime endTime;

    public RoomInfo(Room room) {
        id = room.getId();
        name = room.getName();
        description = room.getDescription();
        capacity = room.getCapacity();

        bookType = room.getBooking().getBookType();

        if (bookType == BookType.DAY) {
            DayBooking booking = (DayBooking) room.getBooking();
            checkIn = booking.getCheckIn();
            checkOut = booking.getCheckOut();
        } else if (bookType == BookType.TIME) {
            TimeBooking booking = (TimeBooking) room.getBooking();
            startTime = booking.getStartTime();
            endTime = booking.getEndTime();
        }
    }

    public static List<RoomInfo> roomList(List<Room> rooms) {
        return rooms.stream().map(RoomInfo::new).toList();
    }

}
