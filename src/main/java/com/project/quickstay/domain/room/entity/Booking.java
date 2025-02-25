package com.project.quickstay.domain.room.entity;

import com.project.quickstay.domain.room.dto.RoomData;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "bookType", discriminatorType = DiscriminatorType.STRING)
public abstract class Booking {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(updatable = false, insertable = false)
    private BookType bookType;

    protected Booking() {
    }

    protected abstract void update(RoomData roomData);

    protected abstract RoomData getUpdateData(RoomData roomData);

    protected static Booking register(RoomData roomData) {
        //bookType에게 새로운 Booking을 생성하는 역할을 할당
        return roomData.getBookType().createBooking(roomData);

        //기존 코드
//        if (getBookType == BookType.DAY) {
//            return new DayBooking(roomData.getCheckIn(), roomData.getCheckOut());
//        }
//        else if (getBookType == BookType.TIME){
//            return new TimeBooking(roomData.getStartTime(), roomData.getEndTime());
//        }
//        else {
//            throw new ServiceException("Wrong BookType");
//        }
    }

    protected void updateBooking(RoomData roomUpdate) {
        update(roomUpdate);
    }

}
