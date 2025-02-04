package com.project.quickstay.domain.room.entity;

import com.project.quickstay.domain.booking.entity.Booking;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.room.dto.RoomRegister;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Setter
    @OneToOne(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Booking booking;

    public Room() {

    }

    public static Room register(Place place, RoomRegister roomRegister) {
        Room room = new Room();
        room.place = place;
        room.name = roomRegister.getName();
        room.description = roomRegister.getDescription();
        room.capacity = roomRegister.getCapacity();
        return room;
    }

}
