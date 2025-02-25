package com.project.quickstay.domain.room.entity;

import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.room.dto.RoomData;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    private String name;

    private String description;

    private Integer capacity;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Booking booking;

    public Room() {

    }

    public static Room register(Place place, RoomData roomData) {
        Room room = new Room();
        room.place = place;
        room.name = roomData.getName();
        room.description = roomData.getDescription();
        room.capacity = roomData.getCapacity();
        room.booking = Booking.register(roomData);
        return room;
    }


    public void updateRoom(RoomData roomData) {
        this.name = roomData.getName();
        this.description = roomData.getDescription();
        this.capacity = roomData.getCapacity();
        this.booking = booking.updateBooking(roomData);
    }

    public RoomData getUpdateData() {
        RoomData roomData = new RoomData();
        roomData.setName(name);
        roomData.setDescription(description);
        roomData.setCapacity(capacity);
        return booking.getUpdateData(roomData);
    }

}
