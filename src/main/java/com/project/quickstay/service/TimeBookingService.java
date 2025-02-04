package com.project.quickstay.service;

import com.project.quickstay.common.BookType;
import com.project.quickstay.domain.booking.entity.Booking;
import com.project.quickstay.domain.booking.entity.TimeBooking;
import com.project.quickstay.domain.room.dto.RoomRegister;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeBookingService implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public Booking register(Room room, RoomRegister roomRegister) {
        TimeBooking timeBooking = new TimeBooking(room, roomRegister.getStartTime(), roomRegister.getEndTime());
        return bookingRepository.save(timeBooking);
    }

    @Override
    public BookType getType() {
        return BookType.TIME;
    }
}
