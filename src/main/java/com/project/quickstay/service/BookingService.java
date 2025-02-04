package com.project.quickstay.service;

import com.project.quickstay.common.BookType;
import com.project.quickstay.domain.booking.entity.Booking;
import com.project.quickstay.domain.room.dto.RoomRegister;
import com.project.quickstay.domain.room.entity.Room;

public interface BookingService {

    Booking register(Room room, RoomRegister roomRegister);

    BookType getType();
}
