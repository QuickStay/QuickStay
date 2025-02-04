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

import java.time.LocalTime;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeBookingService implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public Booking register(Room room, RoomRegister roomRegister) {
        LocalTime startTime = roomRegister.getStartTime();
        LocalTime endTime = roomRegister.getEndTime();
        if (endTime.isBefore(startTime) || endTime.equals(startTime) || startTime.plusHours(1).isAfter(endTime)) {
            /**
             * 1. endTime > startTime
             * 2. endTime = startTime
             * 3. startTime + 1hour > endTime //ex) startTime 7:00, endTime 7:30
             */
            throw new IllegalStateException(); //FIXME
        }
        TimeBooking timeBooking = new TimeBooking(room, roomRegister.getStartTime(), roomRegister.getEndTime());
        return bookingRepository.save(timeBooking);
    }

    @Override
    public BookType getType() {
        return BookType.TIME;
    }
}
