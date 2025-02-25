package com.project.quickstay.domain.room.dto;

import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.domain.room.entity.Room;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyRoomInfo {

    private Long id;
    private String name;
    private String description;
    private int capacity;

    private BookType bookType;

    public MyRoomInfo(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.description = room.getDescription();
        this.capacity = room.getCapacity();

        this.bookType = room.getBooking().getBookType();
    }
}
