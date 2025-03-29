package com.project.quickstay.domain.room.entity;

import com.project.quickstay.domain.reservation.dto.OperatingHours;
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

    protected Booking() {
    }

    public BookType getBookType() {
        return BookType.valueOf(this.getClass().getAnnotation(DiscriminatorValue.class).value());
    }

    protected abstract Booking update(RoomData roomData);

    protected abstract RoomData getUpdateData(RoomData roomData);

    public abstract OperatingHours operatingHours();

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

    protected Booking updateBooking(RoomData roomUpdate) {
        if (roomUpdate.getBookType() == getBookType()) {
            return update(roomUpdate);
        } else {
            return register(roomUpdate);
        }
    }
}
