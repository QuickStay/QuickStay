package com.project.quickstay.service;

import com.project.quickstay.common.BookType;
import com.project.quickstay.domain.booking.entity.Booking;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.room.dto.RoomRegister;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final BookingServiceSelector selector;

    public Room register(Place place, RoomRegister roomRegister) {
        Room room = Room.register(place, roomRegister);
        Booking booking = registerBook(room, roomRegister);
        room.setBooking(booking);
        return roomRepository.save(room);
    }

    private Booking registerBook(Room room, RoomRegister roomRegister) {
        BookingService service = selectBookingService(roomRegister.getBookType());
        return service.register(room, roomRegister);
    }

    private BookingService selectBookingService(BookType bookType) {
        return selector.getService(bookType);
    }
}
