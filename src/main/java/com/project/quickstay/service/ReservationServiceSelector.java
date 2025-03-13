package com.project.quickstay.service;

import com.project.quickstay.domain.room.entity.BookType;
import com.project.quickstay.domain.room.entity.Room;
import com.project.quickstay.exception.ServiceException;
import com.project.quickstay.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReservationServiceSelector {

    private final List<ReservationService> reservationServices;
    private final RoomRepository roomRepository;

    public ReservationService getService(Long roomId) {
        BookType bookType = getBookTypeByRoomId(roomId);

        return reservationServices.stream()
                .filter(service -> service.supports(bookType))
                .findFirst()
                .orElseThrow(() -> new ServiceException("예약 서비스를 찾는데 실패하였습니다."));
    }

    private BookType getBookTypeByRoomId(Long roomId) {
        Optional<Room> getRoom = roomRepository.findById(roomId);
        if (getRoom.isEmpty()) {
            throw new ServiceException("Room이 없습니다.");
        }
        return getRoom.get().getBooking().getBookType();
    }
}
