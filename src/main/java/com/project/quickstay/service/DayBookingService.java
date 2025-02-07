package com.project.quickstay.service;

import com.project.quickstay.common.BookType;
import com.project.quickstay.domain.booking.entity.Booking;
import com.project.quickstay.domain.booking.entity.DayBooking;
import com.project.quickstay.domain.booking.entity.TimeBooking;
import com.project.quickstay.domain.room.dto.RoomRegister;
import com.project.quickstay.domain.room.dto.RoomUpdate;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.repository.BookingRepository;
import com.project.quickstay.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DayBookingService implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    @Override
    public Booking register(Room room, RoomRegister roomRegister) {
        return new DayBooking(room, roomRegister.getCheckIn(), roomRegister.getCheckOut());
    }

    @Override
    public void update(Long roomId, RoomUpdate update) {
        Optional<Booking> getBook = bookingRepository.findById(roomId);
        if (getBook.isEmpty()) {
            throw new IllegalStateException(); //FIXME
        }
        Booking book = getBook.get();
        if (book instanceof TimeBooking) {
            bookingRepository.delete(book);

            Optional<Room> getRoom = roomRepository.findById(roomId);
            if (getRoom.isEmpty()) {
                throw new IllegalStateException(); //FIXME
            }
            Room room = getRoom.get();

            DayBooking dayBooking = new DayBooking(room, update.getCheckIn(), update.getCheckOut());
            room.setBooking(dayBooking);
            bookingRepository.save(dayBooking);
            return;
        }

        DayBooking booking = getBooking(roomId);
        booking.setTime(update.getCheckIn(), update.getCheckOut());
    }

    @Override
    public BookType getType() {
        return BookType.DAY;
    }

    private DayBooking getBooking(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            throw new IllegalStateException(); //FIXME
        }
        return (DayBooking) booking.get();
    }
}
