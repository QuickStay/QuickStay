package com.project.quickstay.service;

import com.project.quickstay.common.BookType;
import com.project.quickstay.domain.booking.entity.Booking;
import com.project.quickstay.domain.booking.entity.DayBooking;
import com.project.quickstay.domain.room.dto.RoomRegister;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DayBookingService implements BookingService {

    private final BookingRepository bookingRepository;
    @Override
    public Booking register(Room room, RoomRegister roomRegister) {
        DayBooking dayBooking = new DayBooking(room, roomRegister.getCheckIn(), roomRegister.getCheckOut());
        return bookingRepository.save(dayBooking);
    }

    @Override
    public BookType getType() {
        return BookType.DAY;
    }
}
