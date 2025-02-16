package com.project.quickstay.service;

import com.project.quickstay.common.BookType;
import com.project.quickstay.domain.booking.entity.Booking;
import com.project.quickstay.domain.booking.entity.DayBooking;
import com.project.quickstay.domain.booking.entity.TimeBooking;
import com.project.quickstay.domain.place.entity.Place;
import com.project.quickstay.domain.room.dto.MyRoomInfo;
import com.project.quickstay.domain.room.dto.RoomRegister;
import com.project.quickstay.domain.room.dto.RoomUpdate;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.domain.user.entity.User;
import com.project.quickstay.exception.ServiceException;
import com.project.quickstay.repository.PlaceRepository;
import com.project.quickstay.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final PlaceRepository placeRepository;
    private final RoomRepository roomRepository;
    private final BookingServiceSelector selector;

    public Room register(User user, Long placeId, RoomRegister roomRegister) {
        Place place = getPlaceById(placeId);
        if (!place.getUser().getId().equals(user.getId())) {
            throw new ServiceException("유저 검증에 실패하였습니다.");
        }
        Room room = Room.register(place, roomRegister);
        Booking booking = registerBook(room, roomRegister);
        room.setBooking(booking);
        return roomRepository.save(room);
    }

    public RoomUpdate getUpdateData(Long roomId) {
        Room room = getRoomById(roomId);
        BookType bookType = room.getBooking().getBookType();

        Booking booking = room.getBooking();
        RoomUpdate roomUpdate = new RoomUpdate();
        roomUpdate.setName(room.getName());
        roomUpdate.setDescription(room.getDescription());
        roomUpdate.setCapacity(room.getCapacity());

        if (bookType == BookType.DAY) {
            DayBooking dayBooking = (DayBooking) booking;
            roomUpdate.setBookType(BookType.DAY);
            roomUpdate.setCheckIn(dayBooking.getCheckIn());
            roomUpdate.setCheckOut(dayBooking.getCheckOut());
        } else if (bookType == BookType.TIME) {
            TimeBooking timeBooking = (TimeBooking) booking;
            roomUpdate.setBookType(BookType.TIME);
            roomUpdate.setStartTime(timeBooking.getStartTime());
            roomUpdate.setEndTime(timeBooking.getEndTime());
        } else {
            throw new ServiceException("예약 타입이 올바르지 않습니다.");
        }
        return roomUpdate;
    }

    public void update(User user, Long id, RoomUpdate roomUpdate) { //룸 정보 변경
        Room room = getRoomById(id);
        validUser(user, room);
        room.update(roomUpdate);
        BookingService service = selectBookingService(roomUpdate.getBookType());
        service.update(id, roomUpdate);
    }

    public void delete(User user, Long id) {
        Room room = getRoomById(id);
        validUser(user, room);
        roomRepository.delete(room);
    }

    public List<MyRoomInfo> getMyRoom(Long placeId) {
        return roomRepository.findMyRoomByPlaceId(placeId);
    }

    private Booking registerBook(Room room, RoomRegister roomRegister) {
        BookingService service = selectBookingService(roomRegister.getBookType());
        return service.register(room, roomRegister);
    }

    private BookingService selectBookingService(BookType bookType) {
        return selector.getService(bookType);
    }

    private Room getRoomById(Long id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isEmpty()) {
            throw new ServiceException("방이 없습니다.");
        }
        return room.get();
    }

    private Place getPlaceById(Long id) {
        Optional<Place> place = placeRepository.findById(id);
        if (place.isEmpty()) {
            throw new ServiceException("장소가 없습니다.");
        }
        return place.get();
    }

    private void validUser(User user, Room room) {
        if (!room.getPlace().getUser().getId().equals(user.getId())) {
            throw new ServiceException("유저 검증에 실패하였습니다.");
        }
    }
}
