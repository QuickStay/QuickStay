package com.project.quickstay.domain.booking.entity;

import com.project.quickstay.common.BookType;
import com.project.quickstay.domain.room.entity.Room;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "bookType", discriminatorType = DiscriminatorType.STRING)
public abstract class Booking {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Room room;

    @Enumerated(value = EnumType.STRING)
    @Column(updatable = false, insertable = false)
    private BookType bookType;

    public Booking(Room room) {
        this.room = room;
    }

    public Booking() {
    }
}
