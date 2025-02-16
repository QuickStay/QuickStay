package com.project.quickstay.service;

import com.project.quickstay.common.BookType;
import com.project.quickstay.domain.booking.entity.Booking;
import com.project.quickstay.domain.booking.entity.DayBooking;
import com.project.quickstay.domain.booking.entity.TimeBooking;
import com.project.quickstay.domain.room.dto.RoomRegister;
import com.project.quickstay.domain.room.dto.RoomUpdate;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.exception.ServiceException;
import com.project.quickstay.repository.BookingRepository;
import com.project.quickstay.repository.RoomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TimeBookingService implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    @Override
    public Booking register(Room room, RoomRegister roomRegister) {
        TimeBooking timeBooking = new TimeBooking(room, roomRegister.getStartTime(), roomRegister.getEndTime());
        return bookingRepository.save(timeBooking);
    }

    @Override
    public void update(Long roomId, RoomUpdate update) {
        Optional<Booking> getBook = bookingRepository.findById(roomId);
        if (getBook.isEmpty()) {
            throw new ServiceException("예약 정보가 없습니다.");
        }
        Booking book = getBook.get();
        if (book instanceof DayBooking) {
            Optional<Room> getRoom = roomRepository.findById(roomId);
            if (getRoom.isEmpty()) {
                throw new ServiceException("방이 없습니다.");
            }

            bookingRepository.delete(book);
            Room room = getRoom.get();

            TimeBooking timeBooking = new TimeBooking(room, update.getStartTime(), update.getEndTime());
            room.setBooking(timeBooking);
            bookingRepository.save(timeBooking);

            return;
        }

        TimeBooking booking = getBooking(roomId);
        booking.setTime(update.getStartTime(), update.getEndTime());
    }

    @Override
    public BookType getType() {
        return BookType.TIME;
    }

    private TimeBooking getBooking(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            throw new ServiceException("예약 정보가 없습니다.");
        }
        return (TimeBooking) booking.get();
    }
}
